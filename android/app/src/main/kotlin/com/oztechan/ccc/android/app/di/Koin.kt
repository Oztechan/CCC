package com.oztechan.ccc.android.app.di

import android.content.Context
import android.os.Build
import co.touchlab.kermit.Logger
import com.oztechan.ccc.ad.di.adModule
import com.oztechan.ccc.analytics.di.analyticsModule
import com.oztechan.ccc.android.app.BuildConfig
import com.oztechan.ccc.billing.di.billingModule
import com.oztechan.ccc.client.configservice.ad.di.clientConfigServiceAdModule
import com.oztechan.ccc.client.configservice.review.di.clientConfigServiceReviewModel
import com.oztechan.ccc.client.core.persistence.di.clientCorePersistenceModule
import com.oztechan.ccc.client.datasource.currency.di.clientDataSourceCurrencyModule
import com.oztechan.ccc.client.datasource.watcher.di.clientDataSourceWatcherModule
import com.oztechan.ccc.client.di.repositoryModule
import com.oztechan.ccc.client.di.viewModelModule
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.service.backend.di.clientServiceBackendModule
import com.oztechan.ccc.client.storage.app.di.clientStorageAppModule
import com.oztechan.ccc.client.storage.calculator.di.clientStorageCalculatorModule
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.datasource.conversion.di.commonDataSourceConversionModule
import com.oztechan.ccc.config.di.configModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun initKoin(context: Context) = startKoin {
    androidContext(context)

    modules(
        module { singleOf(::provideDevice) },
        adModule,
        analyticsModule,
        billingModule,

        viewModelModule,
        repositoryModule,

        configModule,

        // client
        clientCorePersistenceModule,
        clientStorageAppModule,
        clientStorageCalculatorModule,
        clientServiceBackendModule,
        clientConfigServiceAdModule,
        clientConfigServiceReviewModel,
        clientDataSourceCurrencyModule,
        clientDataSourceWatcherModule,

        // common
        commonCoreDatabaseModule,
        commonCoreNetworkModule,
        commonCoreInfrastructureModule,
        commonDataSourceConversionModule
    )
}.also {
    Logger.i { "Koin initialised" }
}

private const val FLAVOR_HUAWEI = "huawei"
private const val FLAVOR_GOOGLE = "google"

@Suppress("KotlinConstantConditions")
private fun provideDevice(): Device = when (BuildConfig.FLAVOR) {
    FLAVOR_GOOGLE -> Device.Android.Google(Build.VERSION.SDK_INT)
    FLAVOR_HUAWEI -> Device.Android.Huawei(Build.VERSION.SDK_INT)
    else -> error("Invalid device type")
}
