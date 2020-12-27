/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import com.github.mustafaozhan.ccc.common.log.kermit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
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
        kermit.d { "BaseViewModel onCleared" }
        viewModelJob.cancelChildren()
    }

    fun <T> Flow<T>.observeEffect(provideNewEffect: (T) -> Unit) = onEach {
        provideNewEffect.invoke(it)
    }.launchIn(viewModelScope)

    fun <T> SharedFlow<T>.observeState(provideNewState: (T) -> Unit) = onEach {
        provideNewState.invoke(it)
    }.launchIn(viewModelScope)
}
