package com.oztechan.ccc.client.viewmodel.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun CoroutineScope.launchIgnored(function: suspend () -> Unit) {
    launch {
        function()
    }
}

inline fun <T> MutableStateFlow<T>.update(function: T.() -> T) {
    update { function(value) }
}
