package com.oztechan.ccc.android.ui.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.oztechan.ccc.android.ui.widget.content.WidgetView
import com.oztechan.ccc.android.viewmodel.widget.WidgetViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppWidget : GlanceAppWidget(), KoinComponent {

    private val viewModel: WidgetViewModel by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetView(state = viewModel.state.collectAsState().value)
        }
    }
}
