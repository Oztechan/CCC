package com.oztechan.ccc.android.ui.widget.components

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider

@Composable
fun ImageView(
    provider: ImageProvider,
    modifier: GlanceModifier = GlanceModifier
) = Image(
    provider = provider,
    contentDescription = "",
    modifier = modifier
)
