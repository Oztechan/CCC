package com.github.mustafaozhan.ccc.common.util

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertTrue

class DateUtilTest {
    @Test
    fun nowAsLongTest() = assertTrue {
        Clock.System.now().toEpochMilliseconds() <= nowAsLong()
    }

    @Test
    fun nowAsInstantTest() = assertTrue {
        Clock.System.now().toEpochMilliseconds() <= nowAsInstant().toEpochMilliseconds()
    }
}
