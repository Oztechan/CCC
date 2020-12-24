/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.base

import kotlinx.coroutines.CoroutineScope

abstract class BaseUseCase {
    lateinit var scope: CoroutineScope

    abstract fun onDestroy()
}
