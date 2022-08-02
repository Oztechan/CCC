/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.util

import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.common.model.CurrencyType
import com.oztechan.ccc.common.model.Rates
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
class CalculatorUtilTest {

    @Test
    fun calculateResult() {
        val date = "12:34:56 01.01.2020"
        val base = "EUR"
        val target = "USD"
        val rates = Rates(base, date, usd = 5.0)

        assertEquals(0.0, rates.calculateResult(target, ""))
        assertEquals(0.0, rates.calculateResult(target, null))
        assertEquals(25.0, rates.calculateResult(target, "5.0"))
        assertEquals(0.0, rates.calculateResult("", "5.0"))
        assertEquals(0.0, null.calculateResult(target, "5.0"))

        val inputOne = "1.0"
        assertEquals(rates.getConversionByName(target), rates.calculateResult(target, inputOne))

        val inputTwo = "2.0"
        assertEquals(
            rates.getConversionByName(target)?.times(2),
            rates.calculateResult(target, inputTwo)
        )
    }

    @Test
    fun getCurrencyConversionByRate() {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        val base = "EUR"
        val rates = Rates(base, null, usd = 5.0)

        assertEquals(
            "1 $base = ${rates.getConversionByName(currency.name)} ${currency.getVariablesOneLine()}",
            currency.getCurrencyConversionByRate(base, rates)
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
            add(Currency(CurrencyType.RON.toString(), "", "", "0.0".toDouble(), true))
            add(Currency(CurrencyType.CZK.toString(), "", "", 0.toDouble(), true))
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
        assertEquals("1 234 567.789", actualDouble1.getFormatted())
        val actualDouble2 = 1234567.7890
        assertEquals("1 234 567.789", actualDouble2.getFormatted())
        val actualDouble3 = 1234567.7891
        assertEquals("1 234 567.7891", actualDouble3.getFormatted(4))
        val actualDouble4 = 1234567.7890
        assertEquals("1 234 567.789", actualDouble4.getFormatted(4))
        val actualDouble5 = 0.000000001
        assertEquals("0.000000001", actualDouble5.getFormatted())
        val actualDouble6 = 0.0000000001
        assertEquals("0", actualDouble6.getFormatted())
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
    fun getConversionByName() {
        val rates = Rates(
            "base", "12.12.21", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0,
            13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0, 21.0, 22.0, 23.0, 24.0, 25.0, 26.0,
            27.0, 28.0, 29.0, 30.0, 31.0, 32.0, 33.0, 34.0, 35.0, 36.0, 37.0, 38.0, 39.0, 40.0,
            41.0, 42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0, 50.0, 51.0, 52.0, 53.0, 54.0,
            55.0, 56.0, 57.0, 58.0, 59.0, 60.0, 61.0, 62.0, 63.0, 64.0, 65.0, 66.0, 67.0, 68.0,
            69.0, 70.0, 71.0, 72.0, 73.0, 74.0, 75.0, 76.0, 77.0, 78.0, 79.0, 80.0, 81.0, 82.0,
            83.0, 84.0, 85.0, 86.0, 87.0, 88.0, 89.0, 90.0, 91.0, 92.0, 93.0, 94.0, 95.0, 96.0,
            97.0, 98.0, 99.0, 100.0, 101.0, 102.0, 103.0, 104.0, 105.0, 106.0, 107.0, 108.0,
            109.0, 110.0, 111.0, 112.0, 113.0, 114.0, 115.0, 116.0, 117.0, 118.0, 119.0, 120.0,
            121.0, 122.0, 123.0, 124.0, 125.0, 126.0, 127.0, 128.0, 129.0, 130.0, 131.0, 132.0,
            133.0, 134.0, 135.0, 136.0, 137.0, 138.0, 139.0, 140.0, 141.0, 142.0, 143.0, 144.0,
            145.0, 146.0, 147.0, 148.0, 149.0, 150.0, 151.0, 152.0, 153.0, 154.0, 155.0, 156.0,
            157.0, 158.0, 159.0, 160.0, 161.0, 162.0, 163.0, 164.0, 165.0, 166.0, 167.0, 168.0,
            169.0, 170.0
        )
        CurrencyType.values().forEachIndexed { index, currencyType ->
            assertEquals((index + 1).toDouble(), rates.getConversionByName(currencyType.toString()))
        }

        assertEquals(0.0, rates.getConversionByName("some string"))
        assertEquals(0.0, rates.getConversionByName(""))
    }
}
