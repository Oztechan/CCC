package com.oztechan.ccc.android.ui.mobile.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.oztechan.ccc.android.ui.mobile.theme.AppTheme

@Composable
fun Preview(
    content: @Composable () -> Unit
) = AppTheme {
    Surface {
        content()
    }
}
