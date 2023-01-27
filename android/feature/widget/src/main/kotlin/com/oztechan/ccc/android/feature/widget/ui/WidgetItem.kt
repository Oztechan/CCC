package com.oztechan.ccc.android.feature.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.oztechan.ccc.android.feature.widget.R
import com.oztechan.ccc.android.feature.widget.ui.components.ImageView
import com.oztechan.ccc.client.core.res.getImageIdByName
import com.oztechan.ccc.common.core.model.Currency

@Composable
fun WidgetItem(
    item: Currency,
) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = item.rate.toString(),
            style = TextStyle(color = ColorProvider(R.color.text), fontSize = 12.sp),
            modifier = GlanceModifier.padding(horizontal = 2.dp)
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        Text(
            text = item.code,
            style = TextStyle(color = ColorProvider(R.color.text), fontSize = 10.sp),
            modifier = GlanceModifier.padding(horizontal = 2.dp)
        )

        ImageView(
            provider = ImageProvider(item.code.getImageIdByName()),
            modifier = GlanceModifier
                .size(32.dp)
                .padding(horizontal = 2.dp)
        )
    }
}
