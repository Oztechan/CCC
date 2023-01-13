package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.client.base.BaseViewModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.util.getFormatted
import com.oztechan.ccc.client.util.getRateFromCode
import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.client.util.toValidList
import com.oztechan.ccc.common.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.service.backend.BackendApiService
import com.oztechan.ccc.common.util.nowAsInstant

class WidgetViewModel(
    private val calculatorStorage: CalculatorStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
) : BaseViewModel() {

    var state = WidgetState(
        currentBase = calculatorStorage.currentBase
    )

    suspend fun refreshWidgetData(changeBaseToNext: Boolean?) {
        if (changeBaseToNext != null) {
            updateBase(changeBaseToNext)
        }

        state = state.copy(
            currencyList = listOf(),
            lastUpdate = "",
            currentBase = ""
        )

        getFreshWidgetData()
    }

    private suspend fun getFreshWidgetData() {
        state = state.copy(
            currentBase = calculatorStorage.currentBase,
            lastUpdate = nowAsInstant().toDateString()
        )

        val conversion = backendApiService
            .getConversion(calculatorStorage.currentBase)
            .conversion

        currencyDataSource.getActiveCurrencies()
            .toUIModelList()
            .filterNot { it.name == calculatorStorage.currentBase }
            .onEach {
                it.rate = conversion.getRateFromCode(it.code)?.getFormatted(calculatorStorage.precision).orEmpty()
            }
            .toValidList(calculatorStorage.currentBase)
            .take(MAXIMUM_NUMBER_OF_CURRENCY)
            .let {
                state = state.copy(currencyList = it)
            }
    }

    private suspend fun updateBase(isToNext: Boolean) {
        val activeCurrencies = currencyDataSource.getActiveCurrencies()
            .toUIModelList()

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
