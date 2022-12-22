package com.oztechan.ccc.android.ui.compose.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
@Suppress("FunctionNaming")
fun ImageView(
    painter: Painter,
    modifier: Modifier = Modifier
) = Image(
    painter = painter,
    contentDescription = "",
    modifier = modifier
)
