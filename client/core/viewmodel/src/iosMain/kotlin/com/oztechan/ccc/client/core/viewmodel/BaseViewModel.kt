/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.core.viewmodel

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

@Suppress("EmptyDefaultConstructor", "unused")
actual open class BaseViewModel actual constructor() {

    protected actual val viewModelScope: CoroutineScope = MainScope()

    init {
        Logger.d { "${this::class.simpleName} init" }
    }

    protected actual open fun onCleared() {
        Logger.d { "${this::class.simpleName} onCleared" }
        viewModelScope.cancel()
    }
}
