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
    fun calculateAdRewardEnd() = nowAsLong().let {
        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(days = VIDEO_REWARD),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.VIDEO.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 1),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.MONTH.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 3),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.QUARTER.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 6),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.HALF_YEAR.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 1),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.YEAR.calculateAdRewardEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 100),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.LIFE_TIME.calculateAdRewardEnd(it).toInstant()
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
