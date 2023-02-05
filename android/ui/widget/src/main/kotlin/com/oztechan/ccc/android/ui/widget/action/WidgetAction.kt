package com.oztechan.ccc.android.ui.widget.action

import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback

enum class WidgetAction {
    OPEN_APP,
    NEXT_BASE,
    PREVIOUS_BASE,
    REFRESH;

    companion object {
        fun String?.getWidgetActionOrNull() = this?.let {
            WidgetAction.values().firstOrNull { widgetAction ->
                it == widgetAction.name
            }
        }

        fun WidgetAction.toActionCallback() = actionRunCallback<WidgetActionCallback>(
            actionParametersOf(
                ActionParameters.Key<String>(WidgetActionCallback.KEY_ACTION) to this.name
            )
        )
    }
}
