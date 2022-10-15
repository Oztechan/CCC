/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.oztechan.ccc.provider.di

import co.touchlab.kermit.Logger
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.di.module.getAnalyticsModule
import com.oztechan.ccc.client.di.module.clientModules
import com.oztechan.ccc.provider.di.module.getIOSModule
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCObject
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import platform.Foundation.NSUserDefaults

fun initKoin(
    userDefaults: NSUserDefaults,
    analyticsManager: AnalyticsManager
) = startKoin {
    modules(
        buildList {
            add(getIOSModule(userDefaults))
            add(getAnalyticsModule(analyticsManager))
            addAll(clientModules)
        }
    )
}.also {
    Logger.i { "Koin initialised" }
}

fun <T> Koin.getDependency(objCObject: ObjCObject): T = when (objCObject) {
    is ObjCClass -> getOriginalKotlinClass(objCObject)
    is ObjCProtocol -> getOriginalKotlinClass(objCObject)
    else -> null
}?.let {
    get(it, null) { parametersOf(it.simpleName) } as T
} ?: error("No dependency found")
