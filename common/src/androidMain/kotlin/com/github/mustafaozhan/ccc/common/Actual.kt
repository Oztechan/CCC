/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual fun runTest(block: suspend () -> Unit) = runBlocking { block() }

actual fun isDebug() = BuildConfig.DEBUG
