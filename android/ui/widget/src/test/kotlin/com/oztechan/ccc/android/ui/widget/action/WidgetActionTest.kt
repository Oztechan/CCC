package com.oztechan.ccc.android.ui.widget.action

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
}
