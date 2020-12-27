/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.ccc.common.log.kermit
import kotlinx.coroutines.CoroutineScope

@Suppress("EmptyDefaultConstructor")
actual open class BaseViewModel actual constructor() : ViewModel() {

    protected actual val clientScope: CoroutineScope = viewModelScope
    actual override fun onCleared() {
        kermit.d { "BaseViewModel onCleared" }
        super.onCleared()
    }
}
