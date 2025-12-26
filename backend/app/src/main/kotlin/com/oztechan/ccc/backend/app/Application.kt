/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.app

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.app.module.apiModule
import com.oztechan.ccc.backend.app.module.koinModule
import com.oztechan.ccc.backend.app.module.ktorModule
import com.oztechan.ccc.backend.app.module.loggerModule
import com.oztechan.ccc.backend.app.module.syncModule
import com.oztechan.ccc.backend.app.util.isProduction
import io.ktor.server.application.serverConfig
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    Logger.i { "ApplicationKt main" }

    val appProperties = serverConfig {
        module {
            loggerModule()
            koinModule()
            ktorModule()
            apiModule()
            syncModule()
        }
    }

    embeddedServer(
        Netty,
        appProperties,
    ) {
        envConfig()
    }.start(true)
}

@Suppress("MagicNumber")
fun ApplicationEngine.Configuration.envConfig() {
    val devHost = "127.0.0.1"
    val prodHost = "0.0.0.0"

    if (isProduction) {
        // Public 8080 for old clients using :8080
        connector {
            host = prodHost
            port = 8080
        }
        // Public 80 for new clients without port
        connector {
            host = prodHost
            port = 80
        }
    } else {
        // Local dev only
        connector {
            host = devHost
            port = 8080
        }
    }
}
