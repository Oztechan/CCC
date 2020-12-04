/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

actual val platformName by lazy { "Android" }
actual val platformVersion = android.os.Build.VERSION.SDK_INT.toString()

actual val platformCoroutineContext: CoroutineContext = Dispatchers.IO
