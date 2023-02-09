package com.oztechan.ccc.backend.app.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.controller.api.di.backendControllerAPIModule
import com.oztechan.ccc.backend.controller.sync.di.backendControllerSyncModule
import com.oztechan.ccc.backend.service.free.di.backendServiceFreeModule
import com.oztechan.ccc.backend.service.premium.di.backendServicePremiumModule
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.datasource.conversion.di.commonDataSourceConversionModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

@Suppress("unused")
internal fun Application.koinModule() {
    Logger.i { "KoinModuleKt Application.koinModule" }

    install(Koin) {
        modules(
            // region Backend modules
            // Service modules
            backendServiceFreeModule,
            backendServicePremiumModule,
            // Controller modules
            backendControllerSyncModule,
            backendControllerAPIModule,
            // endregion

            // region Common modules
            // Core modules
            commonCoreDatabaseModule,
            commonCoreNetworkModule,
            commonCoreInfrastructureModule,
            // DataSource Modules
            commonDataSourceConversionModule
            // endregion
        )
    }.also {
        Logger.i { "Koin initialised" }
    }
}
