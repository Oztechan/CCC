package com.oztechan.ccc.android.widget.action

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.oztechan.ccc.android.widget.AppWidgetReceiver

class NextBaseAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) = context.sendBroadcast(
        Intent(
            context,
            AppWidgetReceiver::class.java
        ).apply {
            action = NEXT_BASE_ACTION
        }
    )

    companion object {
        const val NEXT_BASE_ACTION = "nextBaseAction"
    }
}
