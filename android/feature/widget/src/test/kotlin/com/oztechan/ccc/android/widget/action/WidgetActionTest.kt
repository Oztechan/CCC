package com.oztechan.ccc.android.widget.action

import com.oztechan.ccc.android.feature.widget.action.WidgetAction
import com.oztechan.ccc.android.feature.widget.action.WidgetAction.Companion.getWidgetActionOrNull
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
