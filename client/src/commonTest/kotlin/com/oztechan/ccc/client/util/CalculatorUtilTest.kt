/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.util

import com.oztechan.ccc.client.core.shared.util.getRateFromCode
import com.oztechan.ccc.client.helper.BaseTest
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.common.core.model.CurrencyType
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
internal class CalculatorUtilTest : BaseTest() {

    @Test
    fun calculateRate() {
        val date = "12:34:56 01.01.2020"
        val base = "EUR"
        val target = "USD"
        val conversion = Conversion(base, date, usd = 5.0)

        assertEquals(0.0, conversion.calculateRate(target, ""))
        assertEquals(0.0, conversion.calculateRate(target, null))
        assertEquals(25.0, conversion.calculateRate(target, "5.0"))
        assertEquals(0.0, conversion.calculateRate("", "5.0"))
        assertEquals(0.0, null.calculateRate(target, "5.0"))

        val inputOne = "1.0"
        assertEquals(conversion.getRateFromCode(target), conversion.calculateRate(target, inputOne))

        val inputTwo = "2.0"
        assertEquals(
            conversion.getRateFromCode(target)?.times(2),
            conversion.calculateRate(target, inputTwo)
        )
    }

    @Test
    fun getConversionStringFromBase() {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        val base = "EUR"
        val conversion = Conversion(base, null, usd = 5.0)

        assertEquals(
            "1 $base = ${conversion.getRateFromCode(currency.code)} ${currency.getVariablesOneLine()}",
            currency.getConversionStringFromBase(base, conversion)
        )
    }

    @Test
    fun toValidList() {
        val base = CurrencyType.EUR.toString()

        val list: MutableList<Currency> = mutableListOf()
        list.apply {
            add(Currency(CurrencyType.EUR.toString(), "", "", 1.2, true))
            add(Currency(CurrencyType.USD.toString(), "", "", 1.2, false))
            add(Currency(CurrencyType.TRY.toString(), "", "", Double.NaN, true))
            add(Currency(CurrencyType.GGP.toString(), "", "", 0.0, true))
        }
        assertEquals(mutableListOf(), list.toValidList(base))
        assertEquals(mutableListOf(), null.toValidList(base))
    }

    @Test
    fun toStandardDigits() {
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
            assertEquals("0123456789", it.toStandardDigits(), "actual string $it")
        }
    }

    @Test
    fun indexToNumber() {
        val value = Random.nextInt()
        assertEquals(value + 1, value.indexToNumber())
    }

    @Test
    fun numberToIndex() {
        val value = Random.nextInt()
        assertEquals(value - 1, value.numberToIndex())
    }
}
