package com.oztechan.ccc.android.app.di

import android.content.Context
import android.os.Build
import co.touchlab.kermit.Logger
import com.oztechan.ccc.ad.di.adModule
import com.oztechan.ccc.analytics.di.analyticsModule
import com.oztechan.ccc.android.app.BuildConfig
import com.oztechan.ccc.billing.di.billingModule
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

private const val FLAVOR_HUAWEI = "huawei"
private const val FLAVOR_GOOGLE = "google"

private fun provideDevice(): Device = when (BuildConfig.FLAVOR) {
    FLAVOR_GOOGLE -> Device.Android.Google(Build.VERSION.SDK_INT)
    FLAVOR_HUAWEI -> Device.Android.Huawei(Build.VERSION.SDK_INT)
    else -> error("Invalid device type")
}
