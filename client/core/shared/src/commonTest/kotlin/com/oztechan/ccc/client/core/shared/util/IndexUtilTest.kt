package com.oztechan.ccc.client.core.shared.util

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class IndexUtilTest {

    @Test
    fun indexToNumber() {
        val value = Random.nextInt()
        assertEquals(value + 1, value.indexToNumber())
    }

    @Test
    fun numberToIndex() {
        val value = Random.nextInt()
        assertEquals(value - 1, value.numberToIndex())
    }
}
