package com.oztechan.ccc.android.feature.widget

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import com.oztechan.ccc.android.feature.widget.ui.WidgetView
import com.oztechan.ccc.client.viewmodel.widget.WidgetViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppWidget : GlanceAppWidget(), KoinComponent {

    private val viewModel: WidgetViewModel by inject()

    @Composable
    override fun Content() {
        WidgetView(state = viewModel.state)
    }
}
