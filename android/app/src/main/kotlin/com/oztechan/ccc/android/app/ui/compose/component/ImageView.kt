package com.oztechan.ccc.android.app.ui.compose.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun ImageView(
    painter: Painter,
    modifier: Modifier = Modifier
) = Image(
    painter = painter,
    contentDescription = "",
    modifier = modifier
)
