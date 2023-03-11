package com.oztechan.ccc.android.ui.mobile.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun Int.toText(): String = stringResource(id = this)

@Composable
fun Int.toColor(): Color = colorResource(id = this)

@Composable
fun Int.toPainter(): Painter = painterResource(id = this)
