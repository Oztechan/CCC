package com.oztechan.ccc.client.util

import com.squareup.sqldelight.db.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun CoroutineScope.launchIgnored(function: suspend () -> Unit) {
    launch {
        function()
    }
}

@Suppress("unused") // used in iOS
fun <T> Flow<T>.observeWithCloseable(onChange: ((T) -> Unit)): Closeable {
    val job = Job()
    onEach {
        onChange(it)
    }.launchIn(
        CoroutineScope(Dispatchers.Main + job)
    )
    return object : Closeable {
        override fun close() {
            job.cancel()
        }
    }
}
