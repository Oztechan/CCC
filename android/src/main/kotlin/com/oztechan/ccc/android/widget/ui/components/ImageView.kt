package com.oztechan.ccc.android.widget.ui.components

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider

@Composable
@Suppress("FunctionNaming")
fun ImageView(
    provider: ImageProvider,
    modifier: GlanceModifier = GlanceModifier
) = Image(
    provider = provider,
    contentDescription = "",
    modifier = modifier
)
