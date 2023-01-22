package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.di.backendModule
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.data.service.free.di.commonDataServiceFreeModule
import com.oztechan.ccc.common.data.service.premium.di.commonDataServicePremiumModule
import com.oztechan.ccc.common.di.dataSourceModule
import com.oztechan.ccc.common.di.serviceModule
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

            dataSourceModule,
            serviceModule,
        )
    }.also {
        Logger.i { "Koin initialised" }
    }
}
