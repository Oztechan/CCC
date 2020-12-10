/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.extensions

import com.github.mustafaozhan.ccc.android.util.calculateResult
import com.github.mustafaozhan.ccc.android.util.dropDecimal
import com.github.mustafaozhan.ccc.android.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.android.util.getFormatted
import com.github.mustafaozhan.ccc.android.util.getThroughReflection
import com.github.mustafaozhan.ccc.android.util.removeUnUsedCurrencies
import com.github.mustafaozhan.ccc.android.util.toPercent
import com.github.mustafaozhan.ccc.android.util.toRates
import com.github.mustafaozhan.ccc.android.util.toStandardDigits
import com.github.mustafaozhan.ccc.android.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.android.util.toValidList
import com.github.mustafaozhan.ccc.common.model.Currency
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.Rates
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@Suppress("TooManyFunctions")
@RunWith(JUnit4::class)
class ExtensionTest {

    inner class SubjectModel {
        var someInt = 1
        var someString = "Some String"
    }

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

    @Test
    fun `replace unsupported characters`() {
        assertEquals(",٫ −".toSupportedCharacters(), "..-")
    }

    @Test
    fun `get formatted string from double`() {
        assertEquals(123456.7890.getFormatted(), "123 456.789")
    }

    @Test
    fun `replace localized numbers with numeric ones`() {
        // https://en.wikipedia.org/w/index.php?title=Hindu%E2%80%93Arabic_numeral_system
        listOf(
            "०१२३४५६७८९",
            "૦૧૨૩૪૫૬૭૮૯",
            "੦੧੨੩੪੫੬੭੮੯",
            "༠༡༢༣༤༥༦༧༨༩",
            "০১২৩৪৫৬৭৮৯",
            "೦೧೨೩೪೫೬೭೮೯",
            "୦୧୨୩୪୫୬୭୮୯",
            "൦൧൨൩൪൫൬൭൮൯",
            "௦௧௨௩௪௫௬௭௮௯",
            "౦౧౨౩౪౫౬౭౮౯",
            "០១២៣៤៥៦៧៨៩",
            "๐๑๒๓๔๕๖๗๘๙",
            "໐໑໒໓໔໕໖໗໘໙",
            "၀၁၂၃၄၅၆၇၈၉",
            "٠١٢٣٤٥٦٧٨٩",
            "۰۱۲۳۴۵۶۷۸۹",
            "۰۱۲۳۴۵۶۷۸۹",
            "᠐᠑᠒᠓᠔᠕᠖᠗᠘᠙"
        ).forEach {
            assertEquals(it.toStandardDigits(), "0123456789")
        }
    }

    @Test
    fun `drop decimal point from string`() {
        assertEquals("1234.567".dropDecimal(), "1234")
        assertEquals("7 972.932".dropDecimal(), "7972")
        assertEquals("1 2 3434 432.432 .4334".dropDecimal(), "123434432")
    }

    @Test
    fun `to percent`() {
        assertEquals("200/100*5", ("200%5").toPercent())
    }

    @Test
    fun `get through reflection`() {
        val c = SubjectModel()

        assertEquals(
            c.someString,
            c.getThroughReflection<String>("someString")
        )

        assertEquals(
            c.someInt,
            c.getThroughReflection<Int>("someInt")
        )
    }

    @Test
    fun `currency response to rate`() {
        val base = "EUR"
        val date = SimpleDateFormat(
            "HH:mm:ss MM.dd.yyyy",
            Locale.ENGLISH
        ).format(Date())

        val rates = Rates(base, date, uSD = 5.0)
        val currencyResponse = CurrencyResponse(base, date, rates)
        assertEquals(
            rates,
            currencyResponse.toRates()
        )
    }
}
