package com.oztechan.ccc.common.util

import com.oztechan.ccc.test.BaseTest
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertTrue

internal class DateUtilTest : BaseTest() {
    @Test
    fun nowAsLongTest() = assertTrue {
        Clock.System.now().toEpochMilliseconds() <= nowAsLong()
    }

    @Test
    fun nowAsInstantTest() = assertTrue {
        Clock.System.now().toEpochMilliseconds() <= nowAsInstant().toEpochMilliseconds()
    }
}
