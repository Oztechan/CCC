/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.core.viewmodel

import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel() {
    protected val viewModelScope: CoroutineScope
    protected open fun onCleared()
}
