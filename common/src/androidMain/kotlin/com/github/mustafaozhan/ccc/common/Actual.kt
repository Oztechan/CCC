/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.PlatformType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

actual val platform = PlatformType.ANDROID

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO

actual fun runTest(block: suspend () -> Unit) = runBlocking { block() }

actual fun isDebug() = BuildConfig.DEBUG
