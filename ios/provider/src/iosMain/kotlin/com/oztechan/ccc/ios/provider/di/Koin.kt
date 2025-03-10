/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.ios.provider.di

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.configservice.ad.di.clientConfigServiceAdModule
import com.oztechan.ccc.client.configservice.review.di.clientConfigServiceReviewModel
import com.oztechan.ccc.client.configservice.update.di.clientConfigServiceUpdateModule
import com.oztechan.ccc.client.core.analytics.di.clientCoreAnalyticsModule
import com.oztechan.ccc.client.core.persistence.di.NativeDependencyWrapper
import com.oztechan.ccc.client.core.persistence.di.clientCorePersistenceModule
import com.oztechan.ccc.client.core.shared.Device
import com.oztechan.ccc.client.datasource.currency.di.clientDataSourceCurrencyModule
import com.oztechan.ccc.client.datasource.watcher.di.clientDataSourceWatcherModule
import com.oztechan.ccc.client.repository.adcontrol.di.clientRepositoryAdControlModule
import com.oztechan.ccc.client.repository.appconfig.di.clientRepositoryAppConfigModule
import com.oztechan.ccc.client.service.backend.di.clientServiceBackendModule
import com.oztechan.ccc.client.storage.app.di.clientStorageAppModule
import com.oztechan.ccc.client.storage.calculation.di.clientStorageCalculationModule
import com.oztechan.ccc.client.viewmodel.calculator.di.clientViewModelCalculatorModule
import com.oztechan.ccc.client.viewmodel.currencies.di.clientViewModelCurrenciesModule
import com.oztechan.ccc.client.viewmodel.main.di.clientViewModelMainModule
import com.oztechan.ccc.client.viewmodel.premium.di.clientViewModelPremiumModule
import com.oztechan.ccc.client.viewmodel.selectcurrency.di.clientViewModelSelectCurrencyModule
import com.oztechan.ccc.client.viewmodel.settings.di.clientViewModelSettingsModule
import com.oztechan.ccc.client.viewmodel.watchers.di.clientViewModelWatchersModule
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.datasource.conversion.di.commonDataSourceConversionModule
import com.oztechan.ccc.ios.repository.background.di.iosRepositoryBackgroundModule
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCObject
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

@Suppress("unused")
fun initKoin(userDefaults: NSUserDefaults) = startKoin {
    modules(
        // region Platform modules
        getIOSPlatformModule(userDefaults),
        // endregion

        // region iOS modules
        // Repository modules
        iosRepositoryBackgroundModule,
        // endregion

        // region Client modules
        // Core modules
        clientCoreAnalyticsModule,
        clientCorePersistenceModule,
        // Storage modules
        clientStorageAppModule,
        clientStorageCalculationModule,
        // Service modules
        clientServiceBackendModule,
        // ConfigService modules
        clientConfigServiceAdModule,
        clientConfigServiceReviewModel,
        clientConfigServiceUpdateModule,
        // DataSource modules
        clientDataSourceCurrencyModule,
        clientDataSourceWatcherModule,
        // Service modules
        clientRepositoryAdControlModule,
        // ViewModel modules
        clientViewModelMainModule,
        clientViewModelCalculatorModule,
        clientViewModelCurrenciesModule,
        clientViewModelSettingsModule,
        clientViewModelSelectCurrencyModule,
        clientViewModelWatchersModule,
        clientViewModelPremiumModule,
        // Repository modules
        clientRepositoryAppConfigModule,
        // endregion

        // region Common modules
        // Core modules
        commonCoreDatabaseModule,
        commonCoreNetworkModule,
        commonCoreInfrastructureModule,
        // DataSource modules
        commonDataSourceConversionModule
        // endregion
    )
}.also {
    Logger.v { "Koin initialised" }
}

private fun getIOSPlatformModule(userDefaults: NSUserDefaults) = module {
    single { NativeDependencyWrapper(userDefaults) }
    single<Device> { Device.IOS }
}

@BetaInteropApi
@Suppress("unused")
fun <T> Koin.getDependency(objCObject: ObjCObject): T = when (objCObject) {
    is ObjCClass -> getOriginalKotlinClass(objCObject)
    is ObjCProtocol -> getOriginalKotlinClass(objCObject)
    else -> null
}?.let {
    get(it, null) { parametersOf(it.simpleName) } as T
} ?: error("No dependency found")
