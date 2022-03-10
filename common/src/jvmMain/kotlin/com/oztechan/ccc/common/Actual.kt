/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual fun runTest(block: suspend () -> Unit) = runBlocking { block() }
