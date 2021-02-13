/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.PlatformType
import kotlin.coroutines.CoroutineContext
import org.koin.core.module.Module

expect val platform: PlatformType

expect val platformCoroutineContext: CoroutineContext

expect fun getPlatformCommonModule(useFakes: Boolean): Module

expect fun runTest(block: suspend () -> Unit)
