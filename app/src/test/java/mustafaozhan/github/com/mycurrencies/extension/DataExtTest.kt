/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.extension

import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.Rates
import mustafaozhan.github.com.mycurrencies.util.extension.calculateResult
import mustafaozhan.github.com.mycurrencies.util.extension.getCurrencyConversionByRate
import mustafaozhan.github.com.mycurrencies.util.extension.removeUnUsedCurrencies
import mustafaozhan.github.com.mycurrencies.util.extension.toValidList
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
    fun `remove unused currencies`() {
        val list: MutableList<Currency> = mutableListOf()
        list.apply {
            add(Currency(Currencies.BYR.toString(), "", ""))
            add(Currency(Currencies.LVL.toString(), "", ""))
            add(Currency(Currencies.LTL.toString(), "", ""))
            add(Currency(Currencies.ZMK.toString(), "", ""))
            add(Currency(Currencies.CRYPTO_BTC.toString(), "", ""))
        }
        assertEquals(
            mutableListOf<Currency>(),
            list.removeUnUsedCurrencies()
        )
    }

    @Test
    fun `to valid list`() {
        val base = "EUR"

        val list: MutableList<Currency> = mutableListOf()
        list.apply {
            add(Currency(Currencies.EUR.toString(), "", ""))
            add(Currency(Currencies.LVL.toString(), "", "", isActive = true))
            add(Currency(Currencies.LTL.toString(), "", "", rate = Double.NaN))
            add(Currency(Currencies.LTL.toString(), "", "", rate = 0.0))
        }
        assertEquals(
            mutableListOf<Currency>(),
            list.toValidList(base)
        )
    }
}
