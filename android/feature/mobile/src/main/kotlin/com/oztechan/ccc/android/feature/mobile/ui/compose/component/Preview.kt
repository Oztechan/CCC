package com.oztechan.ccc.android.feature.mobile.ui.compose.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.oztechan.ccc.android.feature.mobile.ui.compose.theme.AppTheme

@Composable
fun Preview(
    content: @Composable () -> Unit
) = AppTheme {
    Surface {
        content()
    }
}
