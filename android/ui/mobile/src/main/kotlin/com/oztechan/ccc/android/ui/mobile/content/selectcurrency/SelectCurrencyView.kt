package com.oztechan.ccc.android.ui.mobile.content.selectcurrency

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun NavHostController.SelectCurrencyView() {
    TextButton(
        onClick = { popBackStack() }
    ) {
        Text("Back")
    }
}
