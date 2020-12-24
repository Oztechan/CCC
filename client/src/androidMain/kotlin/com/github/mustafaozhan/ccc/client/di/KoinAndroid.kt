/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.common.log.kermit
import org.koin.core.KoinApplication
import org.koin.core.module.Module

fun initAndroid(androidModule: Module): KoinApplication = initClient(
    androidModule
).also {
    kermit.d { "KoinAndroid initAndroid" }
}
