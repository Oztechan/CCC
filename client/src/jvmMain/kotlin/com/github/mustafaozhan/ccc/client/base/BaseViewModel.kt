/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

@Suppress("EmptyDefaultConstructor")
actual open class BaseViewModel actual constructor() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope: CoroutineScope = GlobalScope

    protected actual val clientScope: CoroutineScope = viewModelScope

    protected actual open fun onCleared() {
        viewModelJob.cancelChildren()
    }
}
