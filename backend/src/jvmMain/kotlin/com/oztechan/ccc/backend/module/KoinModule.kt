package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.di.module.backendModules
import com.oztechan.ccc.common.di.module.commonModules
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

@Suppress("unused")
internal fun Application.koinModule() {
    Logger.i { "KoinModuleKt Application.koinModule" }

    install(Koin) {
        modules(
            buildList {
                addAll(backendModules)
                addAll(commonModules)
            }
        )
    }.also {
        Logger.i { "Koin initialised" }
    }
}
