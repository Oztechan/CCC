package com.oztechan.ccc.client.core.shared.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

internal class DateUtilTest {

    @Test
    fun nowAsLongTest() = assertTrue {
        Clock.System.now().toEpochMilliseconds() <= nowAsLong()
    }

    @Test
    fun nowAsInstantTest() = assertTrue {
        Clock.System.now().toEpochMilliseconds() <= nowAsInstant().toEpochMilliseconds()
    }

    @Test
    fun nowAsDateStringTest() {
        val nowLong = nowAsLong()
        val nowInstant = nowAsInstant()
        assertEquals(nowInstant.toDateString(), nowAsDateString())
        assertEquals(nowLong.toDateString(), nowAsDateString())
    }

    @Test
    fun isPassed() {
        assertTrue { (nowAsLong() - 1.days.inWholeMilliseconds).isPassed() }
        assertTrue { (nowAsLong() - 1.seconds.inWholeMilliseconds).isPassed() }
        assertFalse { (nowAsLong() + 1.days.inWholeMilliseconds).isPassed() }
        assertFalse { (nowAsLong() + 1.seconds.inWholeMilliseconds).isPassed() }
    }

    @Test
    fun isNotPassed() {
        assertFalse { (nowAsLong() - 1.days.inWholeMilliseconds).isNotPassed() }
        assertFalse { (nowAsLong() - 1.seconds.inWholeMilliseconds).isNotPassed() }
        assertTrue { (nowAsLong() + 1.days.inWholeMilliseconds).isNotPassed() }
        assertTrue { (nowAsLong() + 1.seconds.inWholeMilliseconds).isNotPassed() }
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
    fun doubleDigits() {
        assertEquals("01", 1.toDoubleDigits())
        assertEquals("05", 5.toDoubleDigits())
        assertEquals("09", 9.toDoubleDigits())
        assertEquals("10", 10.toDoubleDigits())
    }
}
