package com.oztechan.ccc.android.viewmodel.widget

import com.oztechan.ccc.client.core.shared.util.getFormatted
import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.core.shared.util.isNotPassed
import com.oztechan.ccc.client.core.shared.util.nowAsDateString
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculation.CalculationStorage

class WidgetViewModel(
    private val calculationStorage: CalculationStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
    private val appStorage: AppStorage
) {

    var state = WidgetState(
        currentBase = calculationStorage.currentBase,
        isPremium = appStorage.premiumEndDate.isNotPassed()
    )

    suspend fun refreshWidgetData(changeBaseToNext: Boolean? = null) {
        if (changeBaseToNext != null) {
            updateBase(changeBaseToNext)
        }

        state = state.copy(
            currencyList = listOf(),
            lastUpdate = "",
            currentBase = calculationStorage.currentBase,
            isPremium = appStorage.premiumEndDate.isNotPassed()
        )

        if (state.isPremium) {
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
            .let {
                state = state.copy(
                    currencyList = it,
                    lastUpdate = nowAsDateString()
                )
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
                it % activeCurrencies.size // it handles index -1 and index size +1
            }

        calculationStorage.currentBase = activeCurrencies[newBaseIndex].code
    }

    companion object {
        private const val MAXIMUM_NUMBER_OF_CURRENCY = 7
    }
}
