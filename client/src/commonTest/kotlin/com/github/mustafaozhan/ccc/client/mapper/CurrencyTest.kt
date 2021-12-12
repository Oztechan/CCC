package com.github.mustafaozhan.ccc.client.mapper

import com.github.mustafaozhan.ccc.client.util.assertAllTrue
import com.github.mustafaozhan.ccc.common.model.Currency
import kotlin.test.Test

class CurrencyTest {

    private val currencyDollar = Currency("Dollar", "American Dollar", "$", 123.123, true)

    @Test
    fun toUIModel() {
        val currencyDollarUIModel = currencyDollar.toUIModel()

        assertAllTrue(
            currencyDollar.name == currencyDollarUIModel.name,
            currencyDollar.longName == currencyDollarUIModel.longName,
            currencyDollar.symbol == currencyDollarUIModel.symbol,
            currencyDollar.rate == currencyDollarUIModel.rate,
            currencyDollar.isActive == currencyDollarUIModel.isActive
        )
    }

    @Test
    fun toUIModelList() {
        val currencyEuro = Currency("Euro", "Euro", "", 321.321, false)
        val currencyList = listOf(currencyDollar, currencyEuro)

        val currencyUIModelList = currencyList.toUIModelList()

        currencyList.zip(currencyUIModelList) { first, second ->
            assertAllTrue(
                first.name == second.name,
                first.longName == second.longName,
                first.symbol == second.symbol,
                first.rate == second.rate,
                first.isActive == second.isActive
            )
        }
    }
}
