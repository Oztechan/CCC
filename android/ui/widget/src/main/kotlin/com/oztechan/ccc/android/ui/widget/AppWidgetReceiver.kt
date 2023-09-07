package com.oztechan.ccc.android.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget: GlanceAppWidget = AppWidget()

    private val analyticsManager: AnalyticsManager by inject()

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

        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_DELETED -> analyticsManager.setUserProperty(
                UserProperty.HasWidget(false.toString())
            )

            AppWidgetManager.ACTION_APPWIDGET_ENABLED -> analyticsManager.setUserProperty(
                UserProperty.HasWidget(true.toString())
            )

            AppWidgetManager.ACTION_APPWIDGET_UPDATE,
            AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED -> refreshData(context)

            else -> Unit
        }
    }

    private fun refreshData(context: Context) = runBlocking {
        GlanceAppWidgetManager(context)
            .getGlanceIds(AppWidget::class.java)
            .iterator()
            .forEach { glanceAppWidget.update(context, it) }
    }
}
