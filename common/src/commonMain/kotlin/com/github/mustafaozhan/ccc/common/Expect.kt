/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import kotlin.coroutines.CoroutineContext

expect val platformCoroutineContext: CoroutineContext

expect fun runTest(block: suspend () -> Unit)

expect fun isDebug(): Boolean
