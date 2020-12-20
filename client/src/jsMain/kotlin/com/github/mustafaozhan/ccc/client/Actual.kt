/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client

import com.github.mustafaozhan.ccc.client.model.ClientPlatformType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

actual var clientPlatformType = ClientPlatformType.JS

actual fun runTest(block: suspend (scope: CoroutineScope) -> Unit): dynamic =
    GlobalScope.promise { block(this) }
