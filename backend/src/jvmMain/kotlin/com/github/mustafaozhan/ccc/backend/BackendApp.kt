/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.backend.di.koin
import com.github.mustafaozhan.ccc.backend.di.modules.controllerModule
import com.github.mustafaozhan.ccc.backend.routes.getCurrencyByName
import com.github.mustafaozhan.ccc.backend.routes.getError
import com.github.mustafaozhan.ccc.backend.routes.getRoot
import com.github.mustafaozhan.ccc.backend.service.startListening
import com.github.mustafaozhan.ccc.common.di.modules.apiModule
import com.github.mustafaozhan.ccc.common.di.modules.getDatabaseModule
import com.github.mustafaozhan.ccc.common.di.modules.getSettingsModule
import com.github.mustafaozhan.logmob.initLogger
import com.github.mustafaozhan.logmob.kermit
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.context.startKoin

// Configs
private const val PORT = 8080
private const val HOST = "127.0.0.1"

fun main() {
    initLogger()

    startKoin {
        modules(
            apiModule,
            getDatabaseModule(),
            getSettingsModule(),
            controllerModule
        )
    }.also {
        koin = it.koin
    }

    embeddedServer(
        Netty,
        port = PORT,
        host = HOST
    ) {

        kermit.d { "BackendApp main" }

        startListening()

        routing {
            kermit.d { "start rooting" }

            getError()
            getRoot()
            getCurrencyByName()
        }
    }.start(wait = true)
}
