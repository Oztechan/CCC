/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

@Suppress("EmptyDefaultConstructor")
actual open class BaseUseCase actual constructor() : ViewModel() {
    protected actual val scope: CoroutineScope = viewModelScope
    actual fun onDestroy() {
        onCleared()
    }
}
