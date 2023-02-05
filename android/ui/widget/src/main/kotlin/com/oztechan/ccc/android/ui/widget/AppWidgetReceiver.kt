package com.oztechan.ccc.android.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.oztechan.ccc.android.ui.widget.action.WidgetAction
import com.oztechan.ccc.android.ui.widget.action.WidgetAction.Companion.getWidgetActionOrNull
import com.oztechan.ccc.client.viewmodel.widget.WidgetViewModel
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget: GlanceAppWidget = AppWidget()

    private val viewModel: WidgetViewModel by inject()

    private fun refreshData(
        context: Context,
        changeBaseToNext: Boolean? = null
    ) = runBlocking {
        GlanceAppWidgetManager(context)
            .getGlanceIds(AppWidget::class.java)
            .firstOrNull()
            ?.let { glanceId ->
                viewModel.refreshWidgetData(changeBaseToNext)
                glanceAppWidget.update(context, glanceId)
            }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        refreshData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action.getWidgetActionOrNull()) {
            WidgetAction.REFRESH -> refreshData(context)
            WidgetAction.NEXT_BASE -> refreshData(context, true)
            WidgetAction.PREVIOUS_BASE -> refreshData(context, false)
            WidgetAction.OPEN_APP ->
                context.packageManager
                    .getLaunchIntentForPackage(context.packageName)
                    ?.apply {
                        addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                    }?.let {
                        context.startActivity(it)
                    }

            else -> error("undefined widget action")
        }
    }
}
