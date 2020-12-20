/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.common.di.initKoin
import org.koin.core.module.Module

fun initKoin(appModule: Module? = null) = initKoin(
    appModule,
    clientModule
)

expect val clientModule: Module
