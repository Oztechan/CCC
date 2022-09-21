package com.oztechan.ccc.analytics.model

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class EventTest : BaseTest() {
    @Test
    fun `event keys are set correctly`() {
        assertEquals("base_change", Event.BaseChange(Param.Base("")).key)
        assertEquals("show_conversion", Event.ShowConversion(Param.Base("")).key)
        assertEquals("offline_sync", Event.OfflineSync.key)
        assertEquals("copy_clipboard", Event.CopyClipboard.key)
    }

    @Test
    fun `event BaseChange params set correctly`() {
        val param = Param.Base("EUR")
        val event = Event.BaseChange(param)

        assertEquals(mapOf(param.key to param.value), event.getParams())
    }

    @Test
    fun `event ShowConversion params set correctly`() {
        val param = Param.Base("EUR")
        val event = Event.ShowConversion(param)

        assertEquals(mapOf(param.key to param.value), event.getParams())
    }

    @Test
    fun `event OfflineSync params set correctly`() {
        val event = Event.OfflineSync
        assertNull(event.getParams())
    }

    @Test
    fun `event CopyClipboard params set correctly`() {
        val event = Event.CopyClipboard
        assertNull(event.getParams())
    }
}
