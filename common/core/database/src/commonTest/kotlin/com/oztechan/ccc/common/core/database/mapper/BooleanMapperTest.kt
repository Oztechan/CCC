package com.oztechan.ccc.common.core.database.mapper

import kotlin.test.Test
import kotlin.test.assertEquals

internal class BooleanMapperTest {
    @Test
    fun toLong() {
        assertEquals(0L, false.toLong())
        assertEquals(1L, true.toLong())
    }
}
