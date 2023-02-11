package com.oztechan.ccc.android.ui.widget.action

import android.appwidget.AppWidgetManager
import com.oztechan.ccc.android.ui.widget.action.WidgetAction.Companion.mapToWidgetAction
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WidgetActionTest {
    @Test
    fun mapToWidgetAction() {
        assertNull("someString".mapToWidgetAction())

        WidgetAction.values().forEach {
            assertEquals(it, it.name.mapToWidgetAction())
        }
    }

    @Test
    fun `AppWidgetManager actions mapped to correct WidgetActions`() {
        assertEquals(WidgetAction.IDLE, AppWidgetManager.ACTION_APPWIDGET_DISABLED.mapToWidgetAction())
        assertEquals(WidgetAction.IDLE, AppWidgetManager.ACTION_APPWIDGET_DELETED.mapToWidgetAction())
        assertEquals(WidgetAction.IDLE, AppWidgetManager.ACTION_APPWIDGET_ENABLED.mapToWidgetAction())

        assertEquals(WidgetAction.IDLE, AppWidgetManager.ACTION_APPWIDGET_BIND.mapToWidgetAction())
        assertEquals(WidgetAction.IDLE, AppWidgetManager.ACTION_APPWIDGET_PICK.mapToWidgetAction())
        assertEquals(WidgetAction.IDLE, AppWidgetManager.ACTION_APPWIDGET_CONFIGURE.mapToWidgetAction())
        assertEquals(WidgetAction.IDLE, AppWidgetManager.ACTION_APPWIDGET_RESTORED.mapToWidgetAction())
        assertEquals(WidgetAction.IDLE, AppWidgetManager.ACTION_APPWIDGET_HOST_RESTORED.mapToWidgetAction())

        assertEquals(WidgetAction.REFRESH, AppWidgetManager.ACTION_APPWIDGET_UPDATE.mapToWidgetAction())
        assertEquals(WidgetAction.REFRESH, AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED.mapToWidgetAction())
    }
}
