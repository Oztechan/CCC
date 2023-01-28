package com.oztechan.ccc.client.viewmodel.util

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("unused") // used in iOS
fun <T> Flow<T>.observeWithCloseable(onChange: ((T) -> Unit)): Closeable {
    val scope = MainScope()
    onEach {
        onChange(it)
    }.launchIn(scope)
    return object : Closeable {
        override fun close() {
            scope.cancel()
        }
    }
}

interface Closeable {
    fun close()
}
