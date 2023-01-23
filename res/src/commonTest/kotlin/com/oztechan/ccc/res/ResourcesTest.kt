package com.oztechan.ccc.res

import kotlin.test.Test
import kotlin.test.assertEquals

internal class ResourcesTest {

    @Test
    fun `toImageFileName lowers the case`() {
        val name1 = "ASD"
        val name2 = "Asd"
        assertEquals(name1.lowercase(), name1.toImageFileName())
        assertEquals(name2.lowercase(), name2.toImageFileName())
    }

    @Test
    fun `toImageFileName replace try keyword to tryy`() {
        val name = "try"
        assertEquals("tryy", name.toImageFileName())
    }

    @Test
    fun `toImageFileName returns unknown if the name is empty`() {
        assertEquals("unknown", "".toImageFileName())
    }
}
