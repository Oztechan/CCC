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
    connector {
        host = "127.0.0.1"
        port = 8080
    }

    System.getenv("CI")
        ?.toBoolean()
        ?.takeIf { it }
        ?.let {
            connector {
                host = "127.0.0.1"
                port = 80
            }
        }
}
