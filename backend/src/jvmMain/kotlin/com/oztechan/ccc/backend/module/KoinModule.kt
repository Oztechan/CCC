package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.di.backendModule
import com.oztechan.ccc.common.di.apiModule
import com.oztechan.ccc.common.di.dataSourceModule
import com.oztechan.ccc.common.di.databaseModule
import com.oztechan.ccc.common.di.dispatcherModule
import com.oztechan.ccc.common.di.scopeModule
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

            databaseModule,
            dataSourceModule,

            apiModule,
            serviceModule,

            dispatcherModule,
            scopeModule
        )
    }.also {
        Logger.i { "Koin initialised" }
    }
}
