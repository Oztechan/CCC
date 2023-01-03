package com.oztechan.ccc.android.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.background
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.oztechan.ccc.android.R
import org.koin.core.component.KoinComponent

class AppWidget : GlanceAppWidget(), KoinComponent {

    @Composable
    override fun Content() {
        Text(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(R.color.color_background)
                .padding(8.dp),
            text = "Hello Glance Widget!"
        )
    }
}
