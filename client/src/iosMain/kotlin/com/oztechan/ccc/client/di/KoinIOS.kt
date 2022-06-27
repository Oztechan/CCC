/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.oztechan.ccc.client.di

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.base.BaseViewModel
import com.oztechan.ccc.client.di.module.clientModule
import com.oztechan.ccc.client.di.module.getIOSModule
import com.oztechan.ccc.common.di.getDependency
import com.oztechan.ccc.common.di.modules.apiModule
import com.oztechan.ccc.common.di.modules.getDatabaseModule
import com.oztechan.ccc.common.di.modules.getSettingsModule
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import platform.Foundation.NSUserDefaults

fun initIOS(userDefaults: NSUserDefaults) = startKoin {
    modules(
        getIOSModule(userDefaults),
        clientModule,
        apiModule,
        getDatabaseModule(),
        getSettingsModule()
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

fun <T> Koin.getDependency(objCClass: ObjCClass): T? = getOriginalKotlinClass(objCClass)?.let {
    getDependency(it)
}

fun <T> Koin.getDependency(objCProtocol: ObjCProtocol): T? = getOriginalKotlinClass(objCProtocol)?.let {
    getDependency(it)
}
