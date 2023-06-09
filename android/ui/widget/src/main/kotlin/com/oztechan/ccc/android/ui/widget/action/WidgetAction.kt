package com.oztechan.ccc.android.ui.widget.action

import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback

enum class WidgetAction {
    OPEN_APP,
    IDLE;

    companion object {
        fun String?.mapToWidgetAction() = WidgetAction.values()
            .firstOrNull { this == it.name }

        fun WidgetAction.toActionCallback() = actionRunCallback<WidgetActionCallback>(
            actionParametersOf(
                ActionParameters.Key<String>(WidgetActionCallback.KEY_ACTION) to this.name
            )
        )
    }
}
