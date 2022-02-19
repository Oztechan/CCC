package com.github.mustafaozhan.ccc.common.util

import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseUtilTest {
    @Test
    fun toDatabaseBoolean() {
        assertEquals(0L, false.toDatabaseBoolean())
        assertEquals(1L, true.toDatabaseBoolean())
    }
}
