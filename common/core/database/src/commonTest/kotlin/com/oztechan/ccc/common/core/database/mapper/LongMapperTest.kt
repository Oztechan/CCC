package com.oztechan.ccc.common.core.database.mapper

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class LongMapperTest {
    @Test
    fun toBoolean() {
        assertTrue { 1L.toBoolean() }
        assertFalse { 0L.toBoolean() }
        assertFailsWith(IllegalStateException::class) {
            2L.toBoolean()
        }
    }
}
