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
    fun toSupportedCharacters() {
        assertEquals("..-", ",٫ −".toSupportedCharacters())
    }

    @Test
    fun getFormatted() {
        val actualDouble1 = 1234567.7891
        assertEquals("1 234 567.789", actualDouble1.getFormatted(3))
        val actualDouble2 = 1234567.7890
        assertEquals("1 234 567.789", actualDouble2.getFormatted(3))
        val actualDouble3 = 1234567.7891
        assertEquals("1 234 567.7891", actualDouble3.getFormatted(4))
        val actualDouble4 = 1234567.7890
        assertEquals("1 234 567.789", actualDouble4.getFormatted(4))
        val actualDouble5 = 0.000000001
        assertEquals("0.000000001", actualDouble5.getFormatted(3))
        val actualDouble6 = 0.0000000001
        assertEquals("0", actualDouble6.getFormatted(3))
    }

    @Test
    fun removeScientificNotation() {
        assertEquals("1234567.789", 1234567.7890.removeScientificNotation())
        assertEquals("1234567.789123", 1234567.7891230.removeScientificNotation())
        assertEquals("1234567.7", 1234567.7.removeScientificNotation())
        assertEquals("0.7", .7.removeScientificNotation())
        assertEquals("7.7", 7.7.removeScientificNotation())
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
