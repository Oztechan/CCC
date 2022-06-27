package com.oztechan.ccc.common.mapper

import kotlin.test.Test
import kotlin.test.assertEquals

class BooleanTest {
    @Test
    fun toLong() {
        assertEquals(0L, false.toLong())
        assertEquals(1L, true.toLong())
    }
}
