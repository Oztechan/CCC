/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren

@Suppress("EmptyDefaultConstructor")
actual open class BaseViewModel actual constructor() {

    protected actual val clientScope: CoroutineScope = MainScope()

    protected actual open fun onCleared() {
        clientScope.coroutineContext.cancelChildren()
    }
}
