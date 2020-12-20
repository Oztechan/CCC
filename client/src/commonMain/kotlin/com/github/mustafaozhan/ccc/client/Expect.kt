/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client

import com.github.mustafaozhan.ccc.client.model.ClientPlatformType
import kotlinx.coroutines.CoroutineScope

expect var clientPlatformType: ClientPlatformType

expect fun runTest(block: suspend (scope: CoroutineScope) -> Unit)
