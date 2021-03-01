package com.github.mustafaozhan.ccc.desktop

import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar


fun main() = Window {

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Currency Converter Calculator") })
            },
            content = {
            }
        )

    }
}