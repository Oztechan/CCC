package com.oztechan.ccc.client.core.shared.util

import com.oztechan.ccc.common.core.infrastructure.constants.DAY
import com.oztechan.ccc.common.core.infrastructure.constants.SECOND
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    fun isItOver() {
        assertTrue { (nowAsLong() - DAY).isItOver() }
        assertTrue { (nowAsLong() - SECOND).isItOver() }
        assertFalse { (nowAsLong() + DAY).isItOver() }
        assertFalse { (nowAsLong() + SECOND).isItOver() }
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
