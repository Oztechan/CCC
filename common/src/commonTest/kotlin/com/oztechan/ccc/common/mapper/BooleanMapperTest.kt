package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BooleanMapperTest : BaseTest() {
    @Test
    fun toLong() {
        assertEquals(0L, false.toLong())
        assertEquals(1L, true.toLong())
    }
}
