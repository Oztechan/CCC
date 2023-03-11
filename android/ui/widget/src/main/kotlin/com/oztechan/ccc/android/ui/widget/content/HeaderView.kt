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
import com.oztechan.ccc.android.ui.widget.action.WidgetAction
import com.oztechan.ccc.android.ui.widget.action.WidgetAction.Companion.toActionCallback
import com.oztechan.ccc.android.ui.widget.components.ImageView
import com.oztechan.ccc.client.core.res.getImageIdByName

@Composable
fun HeaderView(currentBase: String) {
    Row(
        modifier = GlanceModifier.fillMaxWidth().padding(12.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        ImageView(
            provider = ImageProvider(R.drawable.ic_back),
            modifier = GlanceModifier
                .size(20.dp)
                .clickable(WidgetAction.PREVIOUS_BASE.toActionCallback())
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        ImageView(
            provider = ImageProvider(currentBase.getImageIdByName()),
            modifier = GlanceModifier
                .size(32.dp)
                .padding(horizontal = 2.dp)
        )

        Text(
            text = currentBase,
            style = TextStyle(color = ColorProvider(R.color.text), fontSize = 15.sp),
            modifier = GlanceModifier.padding(horizontal = 2.dp)
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        ImageView(
            provider = ImageProvider(R.drawable.ic_next),
            modifier = GlanceModifier
                .size(20.dp)
                .clickable(WidgetAction.NEXT_BASE.toActionCallback())
        )
    }
}
