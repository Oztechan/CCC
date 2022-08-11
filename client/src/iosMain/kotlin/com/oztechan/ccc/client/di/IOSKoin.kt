/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.oztechan.ccc.client.di

import co.touchlab.kermit.Logger
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.client.di.module.clientModules
import com.oztechan.ccc.common.di.NativeDependencyWrapper
import com.oztechan.ccc.common.di.modules.commonModules
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCObject
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initIOS(
    userDefaults: NSUserDefaults,
    analyticsManager: AnalyticsManager
) = startKoin {
    val appModule = module {
        single { NativeDependencyWrapper(userDefaults) }
        single { analyticsManager }
    }

    modules(
        appModule + clientModules + commonModules
    )
}.also {
    Logger.d { "KoinIOS initIOS" }
}

@Suppress("UNCHECKED_CAST")
fun <T> Koin.getDependency(objCObject: ObjCObject): T = when (objCObject) {
    is ObjCClass -> getOriginalKotlinClass(objCObject)
    is ObjCProtocol -> getOriginalKotlinClass(objCObject)
    else -> null
}?.let {
    get(it, null) { parametersOf(it.simpleName) } as T
} ?: error("No dependency found")
