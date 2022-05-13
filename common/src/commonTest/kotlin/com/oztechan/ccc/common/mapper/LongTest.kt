package com.oztechan.ccc.common.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LongTest {
    @Test
    fun toBoolean() {
        assertEquals(true, 1L.toBoolean())
        assertEquals(false, 0L.toBoolean())
        assertFailsWith(IllegalStateException::class) {
            2L.toBoolean()
        }
    }
}
