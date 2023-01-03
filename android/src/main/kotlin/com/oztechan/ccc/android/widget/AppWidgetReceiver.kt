package com.oztechan.ccc.android.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.oztechan.ccc.android.widget.action.RefreshAction
import com.oztechan.ccc.client.viewmodel.widget.WidgetViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget: GlanceAppWidget = AppWidget()

    private val viewModel: WidgetViewModel by inject()

    private val coroutineScope = MainScope()

    private fun refreshData(context: Context) = coroutineScope.launch {
        GlanceAppWidgetManager(context)
            .getGlanceIds(AppWidget::class.java)
            .firstOrNull()
            ?.let { glanceId ->
                viewModel.refreshWidgetData()
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

        if (intent.action == RefreshAction.REFRESH_ACTION) {
            refreshData(context)
        }
    }
}
