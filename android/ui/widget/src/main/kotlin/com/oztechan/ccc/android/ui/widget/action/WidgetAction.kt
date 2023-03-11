package com.oztechan.ccc.android.ui.widget.action

import android.appwidget.AppWidgetManager
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback

enum class WidgetAction {
    OPEN_APP,
    NEXT_BASE,
    PREVIOUS_BASE,
    REFRESH,
    IDLE;

    companion object {
        fun String?.mapToWidgetAction() = when (this) {
            // no needed to refresh
            AppWidgetManager.ACTION_APPWIDGET_DISABLED,
            AppWidgetManager.ACTION_APPWIDGET_DELETED,
            AppWidgetManager.ACTION_APPWIDGET_ENABLED -> IDLE

            // unknown ones perhaps no need to refresh (yet)
            AppWidgetManager.ACTION_APPWIDGET_BIND,
            AppWidgetManager.ACTION_APPWIDGET_PICK,
            AppWidgetManager.ACTION_APPWIDGET_CONFIGURE,
            AppWidgetManager.ACTION_APPWIDGET_RESTORED,
            AppWidgetManager.ACTION_APPWIDGET_HOST_RESTORED -> IDLE

            AppWidgetManager.ACTION_APPWIDGET_UPDATE,
            AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED -> REFRESH

            else -> WidgetAction.values().firstOrNull { this == it.name }
        }

        fun WidgetAction.toActionCallback() = actionRunCallback<WidgetActionCallback>(
            actionParametersOf(
                ActionParameters.Key<String>(WidgetActionCallback.KEY_ACTION) to this.name
            )
        )
    }
}
