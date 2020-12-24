/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("EmptyDefaultConstructor")
actual open class BaseUseCase actual constructor() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + viewModelJob
    )

    protected actual val scope: CoroutineScope = viewModelScope

    actual open fun onDestroy() {
        viewModelJob.cancelChildren()
    }

    fun <T> Flow<T>.onChange(provideNewState: ((T) -> Unit)) {
        onEach {
            provideNewState.invoke(it)
        }.launchIn(scope)
    }

    fun <T> SharedFlow<T>.onChange(provideNewState: ((T) -> Unit)) {
        onEach {
            provideNewState.invoke(it)
        }.launchIn(scope)
    }
}
