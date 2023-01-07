package com.oztechan.ccc.android.ui.compose.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.oztechan.ccc.android.R
import com.oztechan.ccc.android.ui.compose.util.toColor

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    // holo_purple from system used for undiscovered colors
    val colorScheme = ColorScheme(
        primary = R.color.primary.toColor(),
        onPrimary = R.color.background_strong.toColor(),
        primaryContainer = R.color.background.toColor(),
        onPrimaryContainer = R.color.background_weak.toColor(),
        inversePrimary = android.R.color.holo_purple.toColor(),
        secondary = R.color.secondary.toColor(),
        onSecondary = R.color.background_strong.toColor(),
        secondaryContainer = R.color.background.toColor(),
        onSecondaryContainer = R.color.background_weak.toColor(),
        tertiary = R.color.tertiary.toColor(),
        onTertiary = R.color.background_strong.toColor(),
        tertiaryContainer = R.color.background.toColor(),
        onTertiaryContainer = R.color.background_weak.toColor(),
        background = R.color.background.toColor(),
        onBackground = R.color.text.toColor(),
        surface = R.color.background.toColor(),
        onSurface = R.color.text.toColor(),
        surfaceVariant = R.color.background_weak.toColor(),
        onSurfaceVariant = R.color.text_weak.toColor(),
        surfaceTint = android.R.color.holo_purple.toColor(),
        inverseSurface = android.R.color.holo_purple.toColor(),
        inverseOnSurface = android.R.color.holo_purple.toColor(),
        error = R.color.error.toColor(),
        onError = R.color.background_strong.toColor(),
        errorContainer = R.color.background.toColor(),
        onErrorContainer = R.color.background_weak.toColor(),
        outline = R.color.background_weak.toColor(),
        outlineVariant = R.color.text_weak.toColor(),
        scrim = android.R.color.holo_purple.toColor()
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
