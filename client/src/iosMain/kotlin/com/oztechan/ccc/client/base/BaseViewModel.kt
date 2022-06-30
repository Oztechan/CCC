/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.base

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

@Suppress("EmptyDefaultConstructor", "unused")
actual open class BaseViewModel actual constructor() {

    private val viewModelJob = SupervisorJob()

    protected actual val viewModelScope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + viewModelJob
    )

    init {
        Logger.d { "${this::class.simpleName} init" }
    }

    protected actual open fun onCleared() {
        Logger.d { "${this::class.simpleName} onCleared" }
        viewModelJob.cancelChildren()
    }
}
