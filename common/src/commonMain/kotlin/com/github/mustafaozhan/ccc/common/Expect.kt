/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.model.CommonPlatformType
import kotlin.coroutines.CoroutineContext
import org.koin.core.module.Module

expect val commonPlatformType: CommonPlatformType

expect val platformCoroutineContext: CoroutineContext

expect val platformCommonModule: Module
