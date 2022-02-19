package com.github.mustafaozhan.ccc.client.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.launchIgnored(function: suspend () -> Unit) {
    launch {
        function()
    }
}
