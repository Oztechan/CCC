/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data

import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.model.Rates
import com.github.mustafaozhan.data.util.calculateResult
import com.github.mustafaozhan.data.util.getCurrencyConversionByRate
import com.github.mustafaozhan.data.util.removeUnUsedCurrencies
import com.github.mustafaozhan.data.util.toValidList
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DataExtTest {

    @Test
    fun `calculate results by base`() {
        val date = "12:34:56 01.01.2020"
        val base = "EUR"
        val target = "USD"
        val rates = Rates(base, date, uSD = 5.0)

        assertEquals(
            rates.calculateResult(target, "5.0"),
            25.0,
            0.001
        )
    }

    @Test
    fun `get currency conversion by rate`() {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        val base = "EUR"
        val rates = Rates(base, null, uSD = 5.0)

        assertEquals(
            "1 EUR = 5.0 USD Dollar \$",
            currency.getCurrencyConversionByRate(base, rates)
        )
    }

    @Test
    fun `remove unused currencies from currency list`() {
        val list: MutableList<Currency> = mutableListOf()
        list.apply {
            add(Currency(CurrencyType.BYR.toString(), "", ""))
            add(Currency(CurrencyType.LVL.toString(), "", ""))
            add(Currency(CurrencyType.LTL.toString(), "", ""))
            add(Currency(CurrencyType.ZMK.toString(), "", ""))
            add(Currency(CurrencyType.CRYPTO_BTC.toString(), "", ""))
        }
        assertEquals(
            mutableListOf<Currency>(),
            list.removeUnUsedCurrencies()
        )
    }

    @Test
    fun `currency list to valid list`() {
        val base = "EUR"

        val list: MutableList<Currency> = mutableListOf()
        list.apply {
            add(Currency(CurrencyType.EUR.toString(), "", ""))
            add(Currency(CurrencyType.LVL.toString(), "", "", isActive = true))
            add(Currency(CurrencyType.LTL.toString(), "", "", rate = Double.NaN))
            add(Currency(CurrencyType.LTL.toString(), "", "", rate = 0.0))
        }
        assertEquals(
            mutableListOf<Currency>(),
            list.toValidList(base)
        )
    }
}
