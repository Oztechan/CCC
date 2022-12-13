/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initLogger
import com.oztechan.ccc.backend.di.initKoin
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

private const val REQUEST_QUEUE_LIMIT = 48
private const val RUNNING_LIMIT = 30

fun main(args: Array<String>) {
    initLogger()

    Logger.i { "Application main" }

    initKoin()

    embeddedServer(
        factory = Netty,
        environment = commandLineEnvironment(args),
        configure = {
            requestQueueLimit = REQUEST_QUEUE_LIMIT
            runningLimit = RUNNING_LIMIT
        }
    ).start(wait = true)
}
