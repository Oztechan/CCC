/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.extension

import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.util.AD_EXPIRATION
import com.github.mustafaozhan.ccc.client.util.WEEK
import com.github.mustafaozhan.ccc.client.util.calculateResult
import com.github.mustafaozhan.ccc.client.util.doubleDigits
import com.github.mustafaozhan.ccc.client.util.formatToString
import com.github.mustafaozhan.ccc.client.util.getConversionByName
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.isWeekPassed
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toStandardDigits
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.util.toValidList
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.platform
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

@Suppress("TooManyFunctions")
class ExtensionsTest {

    @Test
    fun calculateResult() {
        val date = "12:34:56 01.01.2020"
        val base = "EUR"
        val target = "USD"
        val rates = Rates(base, date, uSD = 5.0)

        assertEquals(25.0, rates.calculateResult(target, "5.0"))
    }

    @Test
    fun getCurrencyConversionByRate() {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        val base = "EUR"
        val rates = Rates(base, null, uSD = 5.0)

        assertEquals(
            "1 EUR = 5.0 USD Dollar \$",
            currency.getCurrencyConversionByRate(base, rates)
        )
    }

    @Test
    fun toValidList() {
        val base = "EUR"

        val list: MutableList<Currency> = mutableListOf()
        list.apply {
            add(Currency(CurrencyType.EUR.toString(), "", ""))
            add(Currency(CurrencyType.LVL.toString(), "", "", isActive = true))
            add(Currency(CurrencyType.LTL.toString(), "", "", rate = Double.NaN))
            add(Currency(CurrencyType.LTL.toString(), "", "", rate = 0.0))
        }
        assertEquals(mutableListOf(), list.toValidList(base))
    }

    @Test
    fun toSupportedCharacters() {
        assertEquals("..-", ",٫ −".toSupportedCharacters())
    }

    @Test
    fun getFormatted() {
        val actualDouble = 123456.7890
        if (platform == PlatformType.ANDROID || platform == PlatformType.JVM) {
            assertEquals("123 456.789", actualDouble.getFormatted())
        } else {
            assertEquals(actualDouble.toString(), actualDouble.getFormatted())
        }
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
        val rates = Rates("EUR", "", uSD = 5.0, eUR = 12.2)

        assertEquals(rates.eUR, rates.getConversionByName("EUR"))
        assertEquals(rates.uSD, rates.getConversionByName("USD"))
    }

    @Test
    fun currencyResponseToRates() {
        val base = "EUR"
        val rates = Rates(base, "", uSD = 5.0)
        val currencyResponse = CurrencyResponse(base, "", rates)
        assertEquals(rates, currencyResponse.toRates())
    }

    @Test
    fun toUnit() {
        assertEquals(Unit, 1.toUnit())
        assertEquals(Unit, 1.0.toUnit())
        assertEquals(Unit, "some text".toUnit())
        assertEquals(Unit, Rates().toUnit())
        assertEquals(Unit, CurrencyResponse("EUR", "", Rates()).toUnit())
        assertEquals(Unit, true.toUnit())
    }

    @Test
    fun isWeekPassed() {
        assertEquals(true, (Clock.System.now().toEpochMilliseconds() - 1 - WEEK).isWeekPassed())
        assertEquals(true, (Clock.System.now().toEpochMilliseconds() - WEEK).isWeekPassed())
        assertEquals(false, (Clock.System.now().toEpochMilliseconds() + 1 - WEEK).isWeekPassed())
    }

    @Test
    fun isRewardExpired() {
        assertEquals(
            true,
            (Clock.System.now().toEpochMilliseconds() - 1 - AD_EXPIRATION).isRewardExpired()
        )
        assertEquals(
            true,
            (Clock.System.now().toEpochMilliseconds() - AD_EXPIRATION).isRewardExpired()
        )
        assertEquals(
            false,
            (Clock.System.now().toEpochMilliseconds() + 1 - AD_EXPIRATION).isRewardExpired()
        )
    }

    @Test
    fun formatToString() {
        assertEquals(
            "09:12 20.12.2020",
            Instant.parse("2020-12-20T09:12:28Z").formatToString(TimeZone.UTC)
        )
    }

    @Test
    fun doubleDigits() {
        assertEquals("01", 1.doubleDigits())
        assertEquals("05", 5.doubleDigits())
        assertEquals("09", 9.doubleDigits())
        assertEquals("10", 10.doubleDigits())
    }
}
