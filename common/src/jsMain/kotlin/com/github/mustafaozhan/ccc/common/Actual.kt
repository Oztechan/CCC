/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformName by lazy { "JS" }
actual val platformVersion = KotlinVersion.CURRENT.toString()

actual val platformCoroutineContext: CoroutineContext = Dispatchers.Default

actual val platformCommonModule: Module = module {
    TODO("JS SqlDelight is not ready yet.")
}
