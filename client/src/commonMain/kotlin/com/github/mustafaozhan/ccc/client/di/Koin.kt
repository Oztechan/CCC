/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.common.di.initCommon
import com.github.mustafaozhan.ccc.common.kermit
import org.koin.core.KoinApplication
import org.koin.core.module.Module

fun initClient(appModule: Module, useFakes: Boolean = false): KoinApplication = initCommon(
    appModule.plus(clientModule), useFakes
).also {
    kermit.d { "Koin initClient" }
}

expect val clientModule: Module
