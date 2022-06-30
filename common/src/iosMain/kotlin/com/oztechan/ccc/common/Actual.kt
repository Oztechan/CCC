/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual fun runTest(block: suspend () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        block()
    }
}
