/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.extension

import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.VIDEO_REWARD
import com.github.mustafaozhan.ccc.client.util.WEEK
import com.github.mustafaozhan.ccc.client.util.calculateAdRewardEnd
import com.github.mustafaozhan.ccc.client.util.calculateResult
import com.github.mustafaozhan.ccc.client.util.doubleDigits
import com.github.mustafaozhan.ccc.client.util.getConversionByName
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.isEmptyOrNullString
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.isWeekPassed
import com.github.mustafaozhan.ccc.client.util.toDateString
import com.github.mustafaozhan.ccc.client.util.toInstant
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toStandardDigits
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.util.toValidList
import com.github.mustafaozhan.ccc.client.util.unitOrNull
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.PlatformType
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.platform
import com.github.mustafaozhan.ccc.common.util.nowAsInstant
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
        val base = CurrencyType.EUR.toString()

        val list: MutableList<Currency> = mutableListOf()
        list.apply {
            add(Currency(CurrencyType.EUR.toString(), "", "", 1.2, true))
            add(Currency(CurrencyType.USD.toString(), "", "", 1.2, false))
            add(Currency(CurrencyType.TRY.toString(), "", "", Double.NaN, true))
            add(Currency(CurrencyType.CZK.toString(), "", "", 0.0, true))
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
        if (platform == PlatformType.JS) {
            assertEquals(actualDouble.toString(), actualDouble.getFormatted())
        } else {
            assertEquals("123 456.789", actualDouble.getFormatted())
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
    fun isEmptyOrNullString() {
        assertEquals(true, "".isEmptyOrNullString())
        assertEquals(true, "null".isEmptyOrNullString())
        assertEquals(true, "Null".isEmptyOrNullString())
        assertEquals(true, "NULL".isEmptyOrNullString())
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
        val rates = Rates(base, nowAsInstant().toDateString(), uSD = 5.0)
        val currencyResponse = CurrencyResponse(base, nowAsInstant().toDateString(), rates)
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
        assertEquals(true, (nowAsLong() - 1 - WEEK).isWeekPassed())
        assertEquals(true, (nowAsLong() - WEEK).isWeekPassed())
        assertEquals(false, (nowAsLong() + 1 - WEEK).isWeekPassed())
    }

    @Test
    fun isRewardExpired() {
        assertFalse { nowAsLong().isRewardExpired() }
        assertFalse { (nowAsLong() + VIDEO_REWARD).isRewardExpired() }
        assertTrue { (nowAsLong() - 1 - VIDEO_REWARD).isRewardExpired() }
        assertTrue { (nowAsLong() - VIDEO_REWARD).isRewardExpired() }
        assertFalse { (nowAsLong() + 1 - VIDEO_REWARD).isRewardExpired() }
    }

    @Test
    fun longToInstant() = assertEquals(
        123.toLong().toInstant(),
        Instant.fromEpochMilliseconds(123)
    )

    @Test
    fun longToDateString() = assertEquals(
        "09:12 20.12.2020",
        1608455548000.toDateString(TimeZone.UTC)
    )

    @Test
    fun instantToDateString() = assertEquals(
        "09:12 20.12.2020",
        Instant.parse("2020-12-20T09:12:28Z").toDateString(TimeZone.UTC)
    )

    @Test
    fun doubleDigits() {
        assertEquals("01", 1.doubleDigits())
        assertEquals("05", 5.doubleDigits())
        assertEquals("09", 9.doubleDigits())
        assertEquals("10", 10.doubleDigits())
    }

    @Test
    fun unit() {
        val nullAny = null
        val notNullAny = Any()
        assertTrue { nullAny.toUnit() == Unit }
        assertTrue { notNullAny.toUnit() == Unit }
        assertTrue { nullAny.unitOrNull() == null }
        assertTrue { notNullAny.unitOrNull() == Unit }
    }

    @Test
    fun calculateAdRewardEnd() {
        assertEquals(
            3,
            RemoveAdType.VIDEO
                .calculateAdRewardEnd()
                .toInstant()
                .minus((nowAsLong() - 100).toInstant(), TimeZone.currentSystemDefault())
                .days
        )
        assertEquals(
            1,
            RemoveAdType.MONTH.calculateAdRewardEnd()
                .toInstant()
                .minus((nowAsLong() - 100).toInstant(), TimeZone.currentSystemDefault())
                .months
        )
        assertEquals(
            3,
            RemoveAdType.QUARTER
                .calculateAdRewardEnd()
                .toInstant()
                .minus((nowAsLong() - 100).toInstant(), TimeZone.currentSystemDefault())
                .months
        )
        assertEquals(
            6,
            RemoveAdType.HALF_YEAR
                .calculateAdRewardEnd()
                .toInstant()
                .minus((nowAsLong() - 100).toInstant(), TimeZone.currentSystemDefault())
                .months
        )
        assertEquals(
            1,
            RemoveAdType.YEAR
                .calculateAdRewardEnd()
                .toInstant()
                .minus((nowAsLong() - 100).toInstant(), TimeZone.currentSystemDefault())
                .years
        )
    }
}
