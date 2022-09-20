package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class LongTest : BaseTest() {
    @Test
    fun toBoolean() {
        assertTrue { 1L.toBoolean() }
        assertFalse { 0L.toBoolean() }
        assertFailsWith(IllegalStateException::class) {
            2L.toBoolean()
        }
    }
}
