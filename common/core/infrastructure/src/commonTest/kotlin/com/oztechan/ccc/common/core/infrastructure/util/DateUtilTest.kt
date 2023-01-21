package com.oztechan.ccc.common.core.infrastructure.util

import kotlinx.datetime.Clock
import kotlin.test.Test
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
}
