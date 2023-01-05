package com.oztechan.ccc.android.ui.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.oztechan.ccc.android.R

@Suppress("FunctionNaming")
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    // holo_purple from system used for undiscovered colors
    val colorScheme = ColorScheme(
        primary = colorResource(id = R.color.primary),
        onPrimary = colorResource(id = R.color.background_strong),
        primaryContainer = colorResource(id = R.color.background),
        onPrimaryContainer = colorResource(id = R.color.background_weak),
        inversePrimary = colorResource(id = android.R.color.holo_purple),
        secondary = colorResource(id = R.color.secondary),
        onSecondary = colorResource(id = R.color.background_strong),
        secondaryContainer = colorResource(id = R.color.background),
        onSecondaryContainer = colorResource(id = R.color.background_weak),
        tertiary = colorResource(id = R.color.tertiary),
        onTertiary = colorResource(id = R.color.background_strong),
        tertiaryContainer = colorResource(id = R.color.background),
        onTertiaryContainer = colorResource(id = R.color.background_weak),
        background = colorResource(id = R.color.background),
        onBackground = colorResource(id = R.color.text),
        surface = colorResource(id = R.color.background),
        onSurface = colorResource(id = R.color.text),
        surfaceVariant = colorResource(id = R.color.background_weak),
        onSurfaceVariant = colorResource(id = R.color.text_weak),
        surfaceTint = colorResource(id = android.R.color.holo_purple),
        inverseSurface = colorResource(id = android.R.color.holo_purple),
        inverseOnSurface = colorResource(id = android.R.color.holo_purple),
        error = colorResource(id = R.color.error),
        onError = colorResource(id = R.color.background_strong),
        errorContainer = colorResource(id = R.color.background),
        onErrorContainer = colorResource(id = R.color.background_weak),
        outline = colorResource(id = R.color.background_weak),
        outlineVariant = colorResource(id = R.color.text_weak),
        scrim = colorResource(id = android.R.color.holo_purple)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
