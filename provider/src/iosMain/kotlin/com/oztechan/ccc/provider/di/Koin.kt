/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.oztechan.ccc.provider.di

import co.touchlab.kermit.Logger
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.di.getAnalyticsModule
import com.oztechan.ccc.client.di.NativeDependencyWrapper
import com.oztechan.ccc.client.di.repositoryModule
import com.oztechan.ccc.client.di.settingsModule
import com.oztechan.ccc.client.di.storageModule
import com.oztechan.ccc.client.di.viewModelModule
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.data.datasource.currency.di.commonDataDatasourceCurrencyModule
import com.oztechan.ccc.common.data.service.backend.di.commonDataServiceBackendModule
import com.oztechan.ccc.common.di.dataSourceModule
import com.oztechan.ccc.config.di.configModule
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCObject
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initKoin(
    userDefaults: NSUserDefaults,
    analyticsManager: AnalyticsManager
) = startKoin {
    modules(
        getIOSModule(userDefaults),
        getAnalyticsModule(analyticsManager),

        viewModelModule,
        repositoryModule,
        settingsModule,
        storageModule,

        configModule,

        // common
        commonCoreDatabaseModule,
        commonCoreNetworkModule,
        commonCoreInfrastructureModule,
        commonDataServiceBackendModule,
        commonDataDatasourceCurrencyModule,

        dataSourceModule
    )
}.also {
    Logger.i { "Koin initialised" }
}

private fun getIOSModule(userDefaults: NSUserDefaults) = module {
    single { NativeDependencyWrapper(userDefaults) }
    single { Device.IOS }
}

fun <T> Koin.getDependency(objCObject: ObjCObject): T = when (objCObject) {
    is ObjCClass -> getOriginalKotlinClass(objCObject)
    is ObjCProtocol -> getOriginalKotlinClass(objCObject)
    else -> null
}?.let {
    get(it, null) { parametersOf(it.simpleName) } as T
} ?: error("No dependency found")
