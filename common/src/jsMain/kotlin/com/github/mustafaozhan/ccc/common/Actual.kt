/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.PlatformType
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platform = PlatformType.JS

actual val platformCoroutineContext: CoroutineContext = Dispatchers.Default

actual val platformCommonModule: Module = module {
    TODO("JS SqlDelight is not ready yet.")
}

actual fun runTest(block: suspend (scope: CoroutineScope) -> Unit): dynamic =
    GlobalScope.promise { block(this) }
