package com.oztechan.ccc.android.ui.mobile.content.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oztechan.ccc.android.ui.mobile.content.selectcurrency.SelectCurrencyView
import com.oztechan.ccc.android.ui.mobile.content.watchers.WatchersView
import com.oztechan.ccc.android.ui.mobile.theme.AppTheme

class ComposeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    with(rememberNavController()) {
                        NavHost(
                            navController = this,
                            startDestination = "watchers"
                        ) {
                            composable("watchers") {
                                WatchersView()
                            }
                            composable("select_currency") {
                                SelectCurrencyView()
                            }
                        }
                    }
                }
            }
        }
    }
}
