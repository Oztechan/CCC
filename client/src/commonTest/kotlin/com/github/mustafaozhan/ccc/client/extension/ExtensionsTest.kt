/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.extension

import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.VIDEO_REWARD
import com.github.mustafaozhan.ccc.client.util.calculateAdRewardEnd
import com.github.mustafaozhan.ccc.client.util.calculateResult
import com.github.mustafaozhan.ccc.client.util.doubleDigits
import com.github.mustafaozhan.ccc.client.util.getConversionByName
import com.github.mustafaozhan.ccc.client.util.getCurrencyConversionByRate
import com.github.mustafaozhan.ccc.client.util.getFormatted
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.isWeekPassed
import com.github.mustafaozhan.ccc.client.util.toDateString
import com.github.mustafaozhan.ccc.client.util.toInstant
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toStandardDigits
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.util.toTodayResponse
import com.github.mustafaozhan.ccc.client.util.toValidList
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.util.DAY
import com.github.mustafaozhan.ccc.common.util.SECOND
import com.github.mustafaozhan.ccc.common.util.WEEK
import com.github.mustafaozhan.ccc.common.util.nowAsInstant
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
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
        val actualDouble = 123456.7890
        assertEquals("123 456.789", actualDouble.getFormatted())
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

    @Test
    fun currencyResponseToRates() {
        val base = "EUR"
        val rates = Rates(base, nowAsInstant().toDateString(), usd = 5.0)
        val currencyResponse = CurrencyResponse(base, nowAsInstant().toDateString(), rates)
        assertEquals(rates, currencyResponse.toRates())
    }

    @Test
    fun currencyResponseToTodayResponse() {
        val currencyResponse = CurrencyResponse("EUR", null, Rates())
        assertEquals(currencyResponse.copy(date = nowAsInstant().toDateString()), currencyResponse.toTodayResponse())
    }

    @Test
    fun isWeekPassed() {
        assertEquals(true, (nowAsLong() - 1 - WEEK).isWeekPassed())
        assertEquals(false, (nowAsLong() + 1 - WEEK).isWeekPassed())
    }

    @Test
    fun isRewardExpired() {
        assertTrue { (nowAsLong() - DAY).isRewardExpired() }
        assertTrue { (nowAsLong() - SECOND).isRewardExpired() }
        assertFalse { (nowAsLong() + DAY).isRewardExpired() }
        assertFalse { (nowAsLong() + SECOND).isRewardExpired() }
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
    fun calculateAdRewardEnd() = nowAsLong().let {
        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(days = VIDEO_REWARD),
                TimeZone.currentSystemDefault()
            ),
            RemoveAdType.VIDEO.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 1),
                TimeZone.currentSystemDefault()
            ),
            RemoveAdType.MONTH.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 3),
                TimeZone.currentSystemDefault()
            ),
            RemoveAdType.QUARTER.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 6),
                TimeZone.currentSystemDefault()
            ),
            RemoveAdType.HALF_YEAR.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 1),
                TimeZone.currentSystemDefault()
            ),
            RemoveAdType.YEAR.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 100),
                TimeZone.currentSystemDefault()
            ),
            RemoveAdType.LIFE_TIME.calculateAdRewardEnd(it).toInstant()
        )
    }
}
