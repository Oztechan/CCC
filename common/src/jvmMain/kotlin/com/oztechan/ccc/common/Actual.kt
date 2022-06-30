/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common

import kotlinx.coroutines.runBlocking

actual fun runTest(block: suspend () -> Unit) = runBlocking { block() }
