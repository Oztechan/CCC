package com.oztechan.ccc.android.app.di

import android.content.Context
import android.os.Build
import co.touchlab.kermit.Logger
import com.oztechan.ccc.android.app.BuildConfig
import com.oztechan.ccc.android.core.ad.di.androidCoreAdModule
import com.oztechan.ccc.android.core.billing.di.androidCoreBillingModule
import com.oztechan.ccc.client.configservice.ad.di.clientConfigServiceAdModule
import com.oztechan.ccc.client.configservice.review.di.clientConfigServiceReviewModel
import com.oztechan.ccc.client.configservice.update.di.clientConfigServiceUpdateModule
import com.oztechan.ccc.client.core.analytics.di.analyticsModule
import com.oztechan.ccc.client.core.persistence.di.clientCorePersistenceModule
import com.oztechan.ccc.client.core.shared.Device
import com.oztechan.ccc.client.datasource.currency.di.clientDataSourceCurrencyModule
import com.oztechan.ccc.client.datasource.watcher.di.clientDataSourceWatcherModule
import com.oztechan.ccc.client.di.viewModelModule
import com.oztechan.ccc.client.repository.adcontrol.di.clientRepositoryAdControlModule
import com.oztechan.ccc.client.repository.appconfig.di.clientRepositoryAppConfigModule
import com.oztechan.ccc.client.service.backend.di.clientServiceBackendModule
import com.oztechan.ccc.client.storage.app.di.clientStorageAppModule
import com.oztechan.ccc.client.storage.calculator.di.clientStorageCalculatorModule
import com.oztechan.ccc.client.viewmodel.widget.di.clientViewModelWidgetModule
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.datasource.conversion.di.commonDataSourceConversionModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun initKoin(context: Context) = startKoin {
    androidContext(context)

    modules(
        getAndroidModule(),
        analyticsModule,

        viewModelModule,

        androidCoreAdModule,
        androidCoreBillingModule,

        // client
        clientCorePersistenceModule,
        clientStorageAppModule,
        clientStorageCalculatorModule,
        clientServiceBackendModule,
        clientConfigServiceAdModule,
        clientConfigServiceUpdateModule,
        clientConfigServiceReviewModel,
        clientDataSourceCurrencyModule,
        clientDataSourceWatcherModule,
        clientRepositoryAdControlModule,
        clientRepositoryAppConfigModule,
        clientViewModelWidgetModule,

        // common
        commonCoreDatabaseModule,
        commonCoreNetworkModule,
        commonCoreInfrastructureModule,
        commonDataSourceConversionModule
    )
}.also {
    Logger.i { "Koin initialised" }
}

private fun getAndroidModule() = module { singleOf(::provideDevice) }

private const val FLAVOR_HUAWEI = "huawei"
private const val FLAVOR_GOOGLE = "google"

@Suppress("KotlinConstantConditions")
private fun provideDevice(): Device = when (BuildConfig.FLAVOR) {
    FLAVOR_GOOGLE -> Device.Android.Google(Build.VERSION.SDK_INT)
    FLAVOR_HUAWEI -> Device.Android.Huawei(Build.VERSION.SDK_INT)
    else -> error("Invalid device type")
}
