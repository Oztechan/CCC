/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.di

import android.content.Context
import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.di.module.clientModules
import com.oztechan.ccc.common.di.modules.commonModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initAndroid(
    context: Context,
    appModules: List<Module>
): KoinApplication = startKoin {
    androidContext(context)

    modules(
        appModules + clientModules + commonModules
    )
}.also {
    Logger.d { "KoinAndroid initAndroid" }
}
