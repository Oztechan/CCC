package com.oztechan.ccc.android.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.oztechan.ccc.android.R
import com.oztechan.ccc.android.widget.ui.components.ImageView
import com.oztechan.ccc.res.getImageResourceIdByName

@Suppress("FunctionNaming")
@Composable
fun HeaderView(currentBase: String) {
    Row(
        modifier = GlanceModifier.padding(4.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        ImageView(
            provider = ImageProvider(getImageResourceIdByName(currentBase)),
            modifier = GlanceModifier
                .size(32.dp)
                .padding(horizontal = 2.dp)
        )

        Text(
            text = currentBase,
            style = TextStyle(color = ColorProvider(R.color.text), fontSize = 13.sp),
            modifier = GlanceModifier.padding(horizontal = 2.dp)
        )
    }
}
