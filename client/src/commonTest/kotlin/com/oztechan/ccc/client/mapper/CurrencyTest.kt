package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CurrencyTest : BaseTest() {

    private val currencyDollar = Currency("Dollar", "American Dollar", "$", 123.123, true)

    @Test
    fun toUIModel() {
        val currencyDollarUIModel = currencyDollar.toUIModel()

        assertEquals(currencyDollar.name, currencyDollarUIModel.name)
        assertEquals(currencyDollar.longName, currencyDollarUIModel.longName)
        assertEquals(currencyDollar.symbol, currencyDollarUIModel.symbol)
        assertEquals(currencyDollar.rate.toString(), currencyDollarUIModel.rate)
        assertEquals(currencyDollar.isActive, currencyDollarUIModel.isActive)
    }

    @Test
    fun toUIModelList() {
        val currencyEuro = Currency("Euro", "Euro", "", 321.321, false)
        val currencyList = listOf(currencyDollar, currencyEuro)

        val currencyUIModelList = currencyList.toUIModelList()

        currencyList.zip(currencyUIModelList) { first, second ->
            assertEquals(first.name, second.name)
            assertEquals(first.longName, second.longName)
            assertEquals(first.symbol, second.symbol)
            assertEquals(first.rate.toString(), second.rate)
            assertEquals(first.isActive, second.isActive)
        }
    }
}
