package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.client.base.BaseViewModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.calculator.CalculatorStorage
import com.oztechan.ccc.client.util.getFormatted
import com.oztechan.ccc.client.util.getRateFromCode
import com.oztechan.ccc.client.util.isPremiumExpired
import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.client.util.toValidList
import com.oztechan.ccc.common.core.infrastructure.util.nowAsInstant
import com.oztechan.ccc.common.data.datasource.currency.CurrencyDataSource
import com.oztechan.ccc.common.service.backend.BackendApiService

class WidgetViewModel(
    private val calculatorStorage: CalculatorStorage,
    private val backendApiService: BackendApiService,
    private val currencyDataSource: CurrencyDataSource,
    private val appStorage: AppStorage
) : BaseViewModel() {

    var state = WidgetState(
        currentBase = calculatorStorage.currentBase,
        isPremium = !appStorage.premiumEndDate.isPremiumExpired()
    )

    suspend fun refreshWidgetData(changeBaseToNext: Boolean? = null) {
        if (changeBaseToNext != null) {
            updateBase(changeBaseToNext)
        }

        state = state.copy(
            currencyList = listOf(),
            lastUpdate = "",
            currentBase = calculatorStorage.currentBase,
            isPremium = !appStorage.premiumEndDate.isPremiumExpired()
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
            .toUIModelList()
            .filterNot { it.name == calculatorStorage.currentBase }
            .onEach {
                it.rate = conversion.getRateFromCode(it.code)?.getFormatted(calculatorStorage.precision).orEmpty()
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
