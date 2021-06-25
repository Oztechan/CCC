/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.PlatformType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.coroutines.CoroutineContext

actual val platform = PlatformType.JS

actual val platformCoroutineContext: CoroutineContext = Dispatchers.Default

actual fun runTest(block: suspend () -> Unit): dynamic = GlobalScope.promise { block() }

// todo need to find correct implementation for JS
@Suppress("FunctionOnlyReturningConstant")
actual fun isDebug() = false
