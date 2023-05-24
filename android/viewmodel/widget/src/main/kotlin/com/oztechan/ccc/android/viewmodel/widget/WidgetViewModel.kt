package com.oztechan.ccc.android.viewmodel.widget

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.shared.util.getFormatted
import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.shared.util.nowAsDateString
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WidgetViewModel(
    private val calculationStorage: CalculationStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
    private val appStorage: AppStorage
) {

    private val _state = MutableStateFlow(
        WidgetState(
            currentBase = calculationStorage.currentBase,
            isPremium = appStorage.premiumEndDate.isNotPassed()
        )
    )
    val state = _state.asStateFlow()

    suspend fun refreshWidgetData(changeBaseToNext: Boolean? = null) {
        Logger.d { "WidgetViewModel refreshWidgetData $changeBaseToNext" }

        if (changeBaseToNext != null) {
            updateBase(changeBaseToNext)
        }

        _state.update {
            it.copy(
                currencyList = listOf(),
                lastUpdate = "",
                currentBase = calculationStorage.currentBase,
                isPremium = appStorage.premiumEndDate.isNotPassed()
            )
        }

        if (_state.value.isPremium) {
            getFreshWidgetData()
        }
    }

    private suspend fun getFreshWidgetData() {
        val conversion = backendApiService
            .getConversion(calculationStorage.currentBase)

        currencyDataSource.getActiveCurrencies()
            .filterNot { it.code == calculationStorage.currentBase }
            .onEach {
                it.rate = conversion.getRateFromCode(it.code)?.getFormatted(calculationStorage.precision).orEmpty()
            }
            .take(MAXIMUM_NUMBER_OF_CURRENCY)
            .let { currencyList ->
                _state.update {
                    it.copy(
                        currencyList = currencyList,
                        lastUpdate = nowAsDateString()
                    )
                }
            }
    }

    private suspend fun updateBase(isToNext: Boolean) {
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

        calculationStorage.currentBase = activeCurrencies[newBaseIndex].code
    }

    companion object {
        private const val MAXIMUM_NUMBER_OF_CURRENCY = 7
    }
}
