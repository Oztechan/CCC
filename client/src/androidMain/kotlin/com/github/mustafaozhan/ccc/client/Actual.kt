/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client

import com.github.mustafaozhan.ccc.client.model.ClientPlatformType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual var clientPlatformType = ClientPlatformType.ANDROID

actual fun runTest(block: suspend (scope: CoroutineScope) -> Unit) = runBlocking { block(this) }
