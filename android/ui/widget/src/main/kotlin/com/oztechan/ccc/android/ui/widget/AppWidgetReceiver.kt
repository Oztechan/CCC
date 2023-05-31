package com.oztechan.ccc.android.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.oztechan.ccc.android.ui.widget.action.WidgetAction
import com.oztechan.ccc.android.ui.widget.action.WidgetAction.Companion.mapToWidgetAction
import com.oztechan.ccc.android.viewmodel.widget.WidgetViewModel
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget: GlanceAppWidget = AppWidget()

    private val viewModel: WidgetViewModel by inject()
    private val analyticsManager: AnalyticsManager by inject()

    private fun refreshData(
        context: Context,
        changeBaseToNext: Boolean? = null
    ) = runBlocking {
        viewModel.event.refreshWidgetData(changeBaseToNext)

        GlanceAppWidgetManager(context)
            .getGlanceIds(AppWidget::class.java)
            .forEach { glanceAppWidget.update(context, it) }
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

        intent.action.let {
            it.mapToWidgetAction()?.executeWidgetAction(context)
                ?: it.executeSystemAction(context)
        }
    }

    private fun WidgetAction.executeWidgetAction(context: Context) = when (this) {
        WidgetAction.IDLE -> Unit
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
    }

    private fun String?.executeSystemAction(context: Context) = when (this) {
        AppWidgetManager.ACTION_APPWIDGET_DELETED -> analyticsManager.setUserProperty(
            UserProperty.HasWidget(false.toString())
        )

        AppWidgetManager.ACTION_APPWIDGET_ENABLED -> analyticsManager.setUserProperty(
            UserProperty.HasWidget(true.toString())
        )

        AppWidgetManager.ACTION_APPWIDGET_UPDATE,
        AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED -> refreshData(context)

        // defined but no action needed system events
        AppWidgetManager.ACTION_APPWIDGET_DISABLED,
        AppWidgetManager.ACTION_APPWIDGET_BIND,
        AppWidgetManager.ACTION_APPWIDGET_PICK,
        AppWidgetManager.ACTION_APPWIDGET_CONFIGURE,
        AppWidgetManager.ACTION_APPWIDGET_RESTORED,
        AppWidgetManager.ACTION_APPWIDGET_HOST_RESTORED -> Unit

        else -> error("undefined widget action")
    }
}
