package com.oztechan.ccc.client.util

import com.oztechan.ccc.client.model.PremiumType
import com.oztechan.ccc.common.util.DAY
import com.oztechan.ccc.common.util.SECOND
import com.oztechan.ccc.common.util.nowAsLong
import com.oztechan.ccc.test.BaseTest
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class DateUtilTest : BaseTest() {
    @Test
    fun isPremiumExpired() {
        assertTrue { (nowAsLong() - DAY).isPremiumExpired() }
        assertTrue { (nowAsLong() - SECOND).isPremiumExpired() }
        assertFalse { (nowAsLong() + DAY).isPremiumExpired() }
        assertFalse { (nowAsLong() + SECOND).isPremiumExpired() }
    }

    @Test
    fun longToInstant() = assertEquals(
        123.toLong().toInstant(),
        Instant.fromEpochMilliseconds(123)
    )

    @Test
    fun toDateString() = assertEquals(
        "09:12 20.12.2020",
        1608455548000.toDateString(TimeZone.UTC)
    )

    @Test
    fun instantToDateString() = assertEquals(
        "09:12 20.12.2020",
        Instant.parse("2020-12-20T09:12:28Z").toDateString(TimeZone.UTC)
    )

    @Test
    fun calculatePremiumEnd() = nowAsLong().let {
        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(days = REWARDED_AD_PREMIUM_IN_DAYS),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.VIDEO.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 1),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.MONTH.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 3),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.QUARTER.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 6),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.HALF_YEAR.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 1),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.YEAR.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 100),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.LIFE_TIME.calculatePremiumEnd(it).toInstant()
        )
    }

    @Test
    fun doubleDigits() {
        assertEquals("01", 1.toDoubleDigits())
        assertEquals("05", 5.toDoubleDigits())
        assertEquals("09", 9.toDoubleDigits())
        assertEquals("10", 10.toDoubleDigits())
    }
}
