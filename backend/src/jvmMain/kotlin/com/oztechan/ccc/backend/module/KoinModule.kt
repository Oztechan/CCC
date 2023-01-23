package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.di.backendModule
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.datasource.conversion.di.commonDataDatasourceConversionModule
import com.oztechan.ccc.common.service.free.di.commonDataServiceFreeModule
import com.oztechan.ccc.common.service.premium.di.commonDataServicePremiumModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

@Suppress("unused")
internal fun Application.koinModule() {
    Logger.i { "KoinModuleKt Application.koinModule" }

    install(Koin) {
        modules(
            backendModule,

            commonCoreDatabaseModule,
            commonCoreNetworkModule,
            commonCoreInfrastructureModule,
            commonDataServiceFreeModule,
            commonDataServicePremiumModule,
            commonDataDatasourceConversionModule
        )
    }.also {
        Logger.i { "Koin initialised" }
    }
}
