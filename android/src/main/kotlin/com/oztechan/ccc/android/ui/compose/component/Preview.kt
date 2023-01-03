package com.oztechan.ccc.android.ui.compose.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.oztechan.ccc.android.ui.compose.theme.AppTheme

@Suppress("FunctionNaming")
@Composable
fun Preview(
    content: @Composable () -> Unit
) = AppTheme {
    Surface {
        content()
    }
}
