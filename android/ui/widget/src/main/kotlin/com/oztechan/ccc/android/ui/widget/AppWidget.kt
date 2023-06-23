package com.oztechan.ccc.android.ui.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import co.touchlab.kermit.Logger
import com.oztechan.ccc.android.ui.widget.content.WidgetView
import com.oztechan.ccc.android.viewmodel.widget.WidgetEffect
import com.oztechan.ccc.android.viewmodel.widget.WidgetViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppWidget : GlanceAppWidget(), KoinComponent {

    private val viewModel: WidgetViewModel by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            LaunchedEffect(key1 = viewModel.effect) {
                viewModel.effect.collect {
                    Logger.i { "AppWidget observeEffects ${it::class.simpleName}" }

                    when (it) {
                        WidgetEffect.OpenApp ->
                            context.packageManager
                                .getLaunchIntentForPackage(context.packageName)
                                ?.apply {
                                    addFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                                    )
                                }?.let { intent -> context.startActivity(intent) }
                    }
                }
            }

            WidgetView(
                state = viewModel.state.collectAsState().value,
                event = viewModel.event
            )
        }
    }
}
