package com.oztechan.ccc.android.ui.widget.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.oztechan.ccc.android.ui.widget.R
import com.oztechan.ccc.android.ui.widget.components.ImageView

@Suppress("RestrictedApi")
@Composable
fun FooterView(
    lastUpdate: String,
    onRefreshClick: () -> Unit,
    onOpenAppClick: () -> Unit
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
    ) {
        ImageView(
            provider = ImageProvider(R.drawable.ic_sync_widget),
            modifier = GlanceModifier
                .size(22.dp)
                .clickable {
                    onRefreshClick()
                }
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        Text(
            text = lastUpdate,
            style = TextStyle(
                color = ColorProvider(R.color.text),
                fontSize = 10.sp
            ),
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        ImageView(
            provider = ImageProvider(R.drawable.ic_app_logo),
            modifier = GlanceModifier
                .size(20.dp)
                .clickable {
                    onOpenAppClick()
                }
        )
    }
}
