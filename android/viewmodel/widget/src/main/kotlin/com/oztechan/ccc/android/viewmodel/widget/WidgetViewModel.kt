package com.oztechan.ccc.android.viewmodel.widget

import co.touchlab.kermit.Logger
import com.oztechan.ccc.android.viewmodel.widget.WidgetData.Companion.MAXIMUM_NUMBER_OF_CURRENCY
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.Param
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import com.oztechan.ccc.client.core.shared.util.getFormatted
import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.shared.util.nowAsDateString
import com.oztechan.ccc.client.core.viewmodel.BaseEffect
import com.oztechan.ccc.client.core.viewmodel.SEEDViewModel
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import kotlinx.coroutines.launch

class WidgetViewModel(
    private val calculationStorage: CalculationStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
    private val appStorage: AppStorage,
    private val analyticsManager: AnalyticsManager
) : SEEDViewModel<WidgetState, BaseEffect, WidgetEvent, WidgetData>(
    initialState = WidgetState(
        base = calculationStorage.currentBase,
        isPremium = appStorage.premiumEndDate.isNotPassed()
    ),
    initialData = WidgetData()
),
    WidgetEvent {

    private fun refreshWidgetData() {
        setState {
            copy(
                currencyList = listOf(),
                lastUpdate = "",
                base = calculationStorage.currentBase,
                isPremium = appStorage.premiumEndDate.isNotPassed()
            )
        }

        if (state.value.isPremium) {
            getFreshWidgetData()
        }
    }

    private fun getFreshWidgetData() = viewModelScope.launch {
        val conversion = backendApiService
            .getConversion(calculationStorage.currentBase)

        currencyDataSource.getActiveCurrencies()
            .filterNot { it.code == calculationStorage.currentBase }
            .onEach {
                it.rate =
                    conversion.getRateFromCode(it.code)?.getFormatted(calculationStorage.precision)
                        .orEmpty()
            }
            .take(MAXIMUM_NUMBER_OF_CURRENCY)
            .let { currencyList ->
                setState {
                    copy(
                        currencyList = currencyList,
                        lastUpdate = nowAsDateString()
                    )
                }
            }
    }

    private fun updateBase(isToNext: Boolean) {
        viewModelScope.launch {
            val activeCurrencies = currencyDataSource.getActiveCurrencies()

            val newBaseIndex = activeCurrencies
                .map { it.code }
                .indexOf(calculationStorage.currentBase)
                .let {
                    if (isToNext) {
                        it + 1
                    } else {
                        it - 1
                    }
                }.let {
                    (it + activeCurrencies.size) % activeCurrencies.size // it handles index -1 and index size +1
                }

            val newBase = activeCurrencies[newBaseIndex].code

            calculationStorage.currentBase = activeCurrencies[newBaseIndex].code

            analyticsManager.trackEvent(Event.BaseChange(Param.Base(newBase)))
            analyticsManager.setUserProperty(UserProperty.BaseCurrency(newBase))

            refreshWidgetData()
        }
    }

    // region Event
    override fun onPreviousClick() {
        Logger.d { "WidgetViewModel onPreviousClick" }
        updateBase(false)
    }

    override fun onNextClick() {
        Logger.d { "WidgetViewModel onNextClick" }
        updateBase(true)
    }

    override fun onRefreshClick() {
        Logger.d { "WidgetViewModel onRefreshClick" }
        refreshWidgetData()
    }

    override fun onOpenAppClick() {
        Logger.d { "WidgetViewModel onOpenAppClick" }
        sendEffect { WidgetEffect.OpenApp }
    }
    // endregion
}
