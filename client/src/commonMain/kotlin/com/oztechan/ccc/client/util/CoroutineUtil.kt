package com.oztechan.ccc.client.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun CoroutineScope.launchIgnored(function: suspend () -> Unit) {
    launch {
        function()
    }
}
