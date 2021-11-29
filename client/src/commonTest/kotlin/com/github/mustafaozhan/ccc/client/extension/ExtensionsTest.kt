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
import com.github.mustafaozhan.ccc.client.util.isEmptyOrNullString
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.isWeekPassed
import com.github.mustafaozhan.ccc.client.util.toDateString
import com.github.mustafaozhan.ccc.client.util.toInstant
import com.github.mustafaozhan.ccc.client.util.toRates
import com.github.mustafaozhan.ccc.client.util.toStandardDigits
import com.github.mustafaozhan.ccc.client.util.toSupportedCharacters
import com.github.mustafaozhan.ccc.client.util.toValidList
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.util.DAY
import com.github.mustafaozhan.ccc.common.util.WEEK
import com.github.mustafaozhan.ccc.common.util.nowAsInstant
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

@Suppress("TooManyFunctions")
class ExtensionsTest {

    @Test
    fun calculateResult() {
        val date = "12:34:56 01.01.2020"
        val base = "EUR"
        val target = "USD"
        val rates = Rates(base, date, usd = 5.0)

        assertEquals(25.0, rates.calculateResult(target, "5.0"))
    }

    @Test
    fun getCurrencyConversionByRate() {
        val currency = Currency("USD", "Dollar", "$", 0.0, true)
        val base = "EUR"
        val rates = Rates(base, null, usd = 5.0)

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
    fun isEmptyOrNullString() {
        assertEquals(true, "".isEmptyOrNullString())
        assertEquals(true, "null".isEmptyOrNullString())
        assertEquals(true, "Null".isEmptyOrNullString())
        assertEquals(true, "NULL".isEmptyOrNullString())
    }

    @Test
    fun getConversionByName() {
        val rates = Rates("EUR", "", usd = 5.0, eur = 12.2)

        assertEquals(rates.eur, rates.getConversionByName("EUR"))
        assertEquals(rates.usd, rates.getConversionByName("USD"))
    }

    @Test
    fun currencyResponseToRates() {
        val base = "EUR"
        val rates = Rates(base, nowAsInstant().toDateString(), usd = 5.0)
        val currencyResponse = CurrencyResponse(base, nowAsInstant().toDateString(), rates)
        assertEquals(rates, currencyResponse.toRates())
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
        assertFalse { (nowAsLong() + VIDEO_REWARD * DAY).isRewardExpired() }
        assertTrue { (nowAsLong() - 1 - VIDEO_REWARD * DAY).isRewardExpired() }
        assertTrue { (nowAsLong() - VIDEO_REWARD * DAY).isRewardExpired() }
        assertFalse { (nowAsLong() + 1 - VIDEO_REWARD * DAY).isRewardExpired() }
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

    @ExperimentalTime
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
            ), RemoveAdType.MONTH.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 3),
                TimeZone.currentSystemDefault()
            ), RemoveAdType.QUARTER.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 6),
                TimeZone.currentSystemDefault()
            ), RemoveAdType.HALF_YEAR.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 1),
                TimeZone.currentSystemDefault()
            ), RemoveAdType.YEAR.calculateAdRewardEnd(it).toInstant()
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
