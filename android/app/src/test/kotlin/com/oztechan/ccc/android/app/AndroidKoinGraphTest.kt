package com.oztechan.ccc.android.app

import android.content.Context
import com.oztechan.ccc.android.core.ad.di.androidCoreAdModule
import com.oztechan.ccc.android.core.billing.di.androidCoreBillingModule
import com.oztechan.ccc.android.viewmodel.widget.di.androidViewModelWidgetModule
import com.oztechan.ccc.client.configservice.ad.di.clientConfigServiceAdModule
import com.oztechan.ccc.client.configservice.review.di.clientConfigServiceReviewModel
import com.oztechan.ccc.client.configservice.update.di.clientConfigServiceUpdateModule
import com.oztechan.ccc.client.core.analytics.di.clientCoreAnalyticsModule
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
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import kotlin.test.Test

internal class AndroidKoinGraphTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `android Koin graph has all dependencies declared`() {
        module {
            includes(
                // Android modules
                androidCoreAdModule,
                androidCoreBillingModule,
                androidViewModelWidgetModule,
                // Client modules
                clientCoreAnalyticsModule,
                clientCorePersistenceModule,
                clientStorageAppModule,
                clientStorageCalculationModule,
                clientServiceBackendModule,
                clientConfigServiceAdModule,
                clientConfigServiceUpdateModule,
                clientConfigServiceReviewModel,
                clientDataSourceCurrencyModule,
                clientDataSourceWatcherModule,
                clientRepositoryAdControlModule,
                clientRepositoryAppConfigModule,
                clientViewModelMainModule,
                clientViewModelCalculatorModule,
                clientViewModelCurrenciesModule,
                clientViewModelSettingsModule,
                clientViewModelSelectCurrencyModule,
                clientViewModelWatchersModule,
                clientViewModelPremiumModule,
                // Common modules
                commonCoreDatabaseModule,
                commonCoreNetworkModule,
                commonCoreInfrastructureModule,
                commonDataSourceConversionModule
            )
        }.verify(
            extraTypes = listOf(
                // Provided by the private platform module / Ktor at runtime, not by the modules above.
                Device::class,
                Context::class,
                HttpClientEngine::class
            )
        )
    }
}
