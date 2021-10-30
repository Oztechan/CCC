/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope

@Suppress("EmptyDefaultConstructor")
actual open class BaseViewModel actual constructor() : ViewModel() {

    protected actual val clientScope: CoroutineScope = viewModelScope

    init {
        Logger.d { "${this::class.simpleName} init" }
    }

    actual override fun onCleared() {
        Logger.d { "${this::class.simpleName} onCleared" }
        super.onCleared()
    }
}
