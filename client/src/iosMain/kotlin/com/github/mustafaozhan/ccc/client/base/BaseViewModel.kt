/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("EmptyDefaultConstructor", "unused")
actual open class BaseViewModel actual constructor() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + viewModelJob
    )

    protected actual val clientScope: CoroutineScope = viewModelScope

    protected actual open fun onCleared() {
        viewModelJob.cancelChildren()
    }

    fun <T> Flow<T>.observe(onChange: ((T) -> Unit)): Closeable {
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
}
