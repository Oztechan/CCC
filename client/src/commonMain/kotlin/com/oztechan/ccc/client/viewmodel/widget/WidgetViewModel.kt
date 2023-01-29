package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.core.shared.util.isItOver
import com.oztechan.ccc.client.core.shared.util.nowAsInstant
import com.oztechan.ccc.client.core.shared.util.toDateString
import com.oztechan.ccc.client.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.client.service.backend.BackendApiService
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.util.toValidList
import com.oztechan.ccc.client.viewmodel.util.getFormatted

class WidgetViewModel(
    private val calculatorStorage: CalculatorStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
    private val appStorage: AppStorage
) {

    var state = WidgetState(
        currentBase = calculatorStorage.currentBase,
        isPremium = !appStorage.premiumEndDate.isItOver()
    )

    suspend fun refreshWidgetData(changeBaseToNext: Boolean? = null) {
        if (changeBaseToNext != null) {
            updateBase(changeBaseToNext)
        }

        state = state.copy(
            currencyList = listOf(),
            lastUpdate = "",
            currentBase = calculatorStorage.currentBase,
            isPremium = !appStorage.premiumEndDate.isItOver()
        )

        if (state.isPremium) {
            getFreshWidgetData()
        }
    }

    private suspend fun getFreshWidgetData() {
        val conversion = backendApiService
            .getConversion(calculatorStorage.currentBase)
            .conversion

        currencyDataSource.getActiveCurrencies()
            .filterNot { it.name == calculatorStorage.currentBase }
            .onEach {
                it.rate = conversion.getRateFromCode(it.code)
                    ?.getFormatted(calculatorStorage.precision)
                    ?.toDoubleOrNull() ?: 0.0
            }
            .toValidList(calculatorStorage.currentBase)
            .take(MAXIMUM_NUMBER_OF_CURRENCY)
            .let {
                state = state.copy(
                    currencyList = it,
                    lastUpdate = nowAsInstant().toDateString()
                )
            }
    }

    private suspend fun updateBase(isToNext: Boolean) {
        val activeCurrencies = currencyDataSource.getActiveCurrencies()

        var newBaseIndex = activeCurrencies
            .map { it.code }
            .indexOf(calculatorStorage.currentBase)

        if (isToNext) {
            newBaseIndex++
        } else {
            newBaseIndex--
        }

        when (newBaseIndex) {
            activeCurrencies.size -> newBaseIndex = 0
            -1 -> newBaseIndex = activeCurrencies.lastIndex
        }
        calculatorStorage.currentBase = activeCurrencies[newBaseIndex].code
    }

    companion object {
        private const val MAXIMUM_NUMBER_OF_CURRENCY = 7
    }
}
