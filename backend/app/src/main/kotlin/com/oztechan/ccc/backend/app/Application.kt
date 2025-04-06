/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.app

import co.touchlab.kermit.Logger
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.EngineMain
import io.ktor.server.netty.Netty

private const val RUNNING_LIMIT = 30

fun main(args: Array<String>) {
    Logger.i { "ApplicationKt main" }

    EngineMain.main(args)

    embeddedServer(
        factory = Netty,
        configure = {
            runningLimit = RUNNING_LIMIT
        }
    ).start(wait = true)
}
