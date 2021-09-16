/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.PlatformType
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

actual val platform = PlatformType.JVM

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual fun runTest(block: suspend () -> Unit) = runBlocking { block() }

// todo need to find correct implementation for JVM
@Suppress("FunctionOnlyReturningConstant")
actual fun isDebug() = false
