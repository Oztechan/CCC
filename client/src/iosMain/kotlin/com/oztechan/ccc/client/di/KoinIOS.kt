/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.oztechan.ccc.client.di

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.base.BaseViewModel
import com.oztechan.ccc.client.di.module.clientModule
import com.oztechan.ccc.client.di.module.getIOSModule
import com.oztechan.ccc.common.di.modules.apiModule
import com.oztechan.ccc.common.di.modules.databaseModule
import com.oztechan.ccc.common.di.modules.dispatcherModule
import com.oztechan.ccc.common.di.modules.settingsModule
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCObject
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import platform.Foundation.NSUserDefaults

fun initIOS(userDefaults: NSUserDefaults) = startKoin {
    modules(
        getIOSModule(userDefaults),
        clientModule,
        apiModule,
        databaseModule,
        settingsModule,
        dispatcherModule
    )
}.also {
    Logger.d { "KoinIOS initIOS" }
}

actual inline fun <reified T : BaseViewModel> Module.viewModelDefinition(
    qualifier: Qualifier?,
    createdAtStart: Boolean,
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>> = single(
    qualifier = qualifier,
    createdAtStart = createdAtStart,
    definition = definition
)

@Suppress("UNCHECKED_CAST")
fun <T> Koin.getDependency(objCObject: ObjCObject): T = when (objCObject) {
    is ObjCClass -> getOriginalKotlinClass(objCObject)
    is ObjCProtocol -> getOriginalKotlinClass(objCObject)
    else -> null
}?.let {
    get(it, null) { parametersOf(it.simpleName) } as T
} ?: error("No dependency found")
