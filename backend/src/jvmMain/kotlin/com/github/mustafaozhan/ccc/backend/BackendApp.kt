/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.backend.di.Koin
import com.github.mustafaozhan.ccc.backend.routes.getCurrencyByName
import com.github.mustafaozhan.ccc.backend.routes.getError
import com.github.mustafaozhan.ccc.backend.routes.getRoot
import com.github.mustafaozhan.logmob.kermit
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

// Configs
private const val PORT = 8080
private const val HOST = "127.0.0.1"

private val apiController = Koin.getApiController()
private val rootingController = Koin.getRootingController()

fun main() {
    embeddedServer(
        Netty,
        port = PORT,
        host = HOST
    ) {

        kermit.d { "BackendApp main" }

        apiController.startSyncApi()

        routing {
            kermit.d { "start rooting" }

            getError()
            getRoot()
            getCurrencyByName(rootingController)
        }
    }.start(wait = true)
}
