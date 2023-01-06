package com.oztechan.ccc.client.viewmodel.widget

import com.oztechan.ccc.client.base.BaseViewModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.model.Currency
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
    lateinit var currencyList: List<Currency>
    lateinit var lastUpdate: String
    lateinit var currentBase: String

    suspend fun refreshWidgetData() {
        lastUpdate = ""
        currentBase = ""
        currencyList = listOf()

        currentBase = calculatorStorage.currentBase
        lastUpdate = nowAsInstant().toDateString()

        val conversion = backendApiService
            .getConversion(calculatorStorage.currentBase)
            .conversion

        currencyList = currencyDataSource.getActiveCurrencies()
            .toUIModelList()
            .filterNot { it.name == currentBase }
            .onEach {
                it.rate = conversion.getRateFromCode(it.code)?.getFormatted(calculatorStorage.precision).orEmpty()
            }
            .toValidList(calculatorStorage.currentBase)
            .take(MAXIMUM_NUMBER_OF_CURRENCY)
    }

    companion object {
        private const val MAXIMUM_NUMBER_OF_CURRENCY = 7
    }
}
