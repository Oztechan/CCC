/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("EmptyDefaultConstructor", "unused")
actual open class BaseViewModel actual constructor() {

    protected actual val clientScope: CoroutineScope = MainScope()

    protected actual open fun onCleared() {
        clientScope.coroutineContext.cancelChildren()
    }Pl

    fun <T> Flow<T>.observeEffect(provideNewEffect: (T) -> Unit) = onEach {
        provideNewEffect.invoke(it)
    }.launchIn(clientScope)

    fun <T> SharedFlow<T>.observeState(provideNewState: (T) -> Unit) = onEach {
        provideNewState.invoke(it)
    }.launchIn(clientScope)
}
