package com.oztechan.ccc.android.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.oztechan.ccc.android.R
import com.oztechan.ccc.android.widget.action.RefreshAction
import com.oztechan.ccc.client.viewmodel.widget.WidgetViewModel

@Suppress("FunctionNaming")
@Composable
fun WidgetView(viewModel: WidgetViewModel) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(R.color.background),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        Text(
            text = viewModel.currentBase,
            style = TextStyle(color = ColorProvider(R.color.text), fontSize = 13.sp),
            modifier = GlanceModifier.padding(horizontal = 2.dp)
        )

        Image(
            provider = ImageProvider(R.drawable.ic_sync_widget),
            contentDescription = LocalContext.current.getString(R.string.app_name),
            modifier = GlanceModifier
                .size(24.dp)
                .clickable(actionRunCallback<RefreshAction>())
        )
    }
}
