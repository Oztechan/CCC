package com.oztechan.ccc.android.ui.widget.action

import com.oztechan.ccc.android.ui.widget.action.WidgetAction.Companion.getWidgetActionOrNull
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WidgetActionTest {
    @Test
    fun getWidgetActionOrNull() {
        assertNull("someString".getWidgetActionOrNull())

        WidgetAction.values().forEach {
            assertEquals(it, it.name.getWidgetActionOrNull())
        }
    }
}
