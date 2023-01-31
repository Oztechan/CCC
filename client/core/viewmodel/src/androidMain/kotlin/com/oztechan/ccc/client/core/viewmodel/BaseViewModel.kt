/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope

@Suppress("EmptyDefaultConstructor")
actual open class BaseViewModel actual constructor() : ViewModel() {

    protected actual val viewModelScope: CoroutineScope by lazy {
        ViewModel::viewModelScope.get(this)
    }

    init {
        Logger.d { "${this::class.simpleName} init" }
    }

    actual override fun onCleared() {
        Logger.d { "${this::class.simpleName} onCleared" }
        super.onCleared()
    }
}
