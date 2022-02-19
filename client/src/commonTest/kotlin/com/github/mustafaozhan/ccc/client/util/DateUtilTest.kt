package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.common.util.DAY
import com.github.mustafaozhan.ccc.common.util.SECOND
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateUtilTest {
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
