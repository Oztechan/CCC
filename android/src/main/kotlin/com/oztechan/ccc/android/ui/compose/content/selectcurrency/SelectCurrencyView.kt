package com.oztechan.ccc.android.ui.compose.content.selectcurrency

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Suppress("FunctionNaming")
@Composable
fun NavHostController.SelectCurrencyView() {
    TextButton(
        onClick = { popBackStack() }
    ) {
        Text("Back")
    }
}
