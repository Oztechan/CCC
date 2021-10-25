/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.backend.controller.ApiController
import com.github.mustafaozhan.ccc.backend.di.koin
import com.github.mustafaozhan.ccc.backend.di.modules.controllerModule
import com.github.mustafaozhan.ccc.backend.routes.getCurrencyByName
import com.github.mustafaozhan.ccc.backend.routes.getError
import com.github.mustafaozhan.ccc.backend.routes.getRoot
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.di.modules.apiModule
import com.github.mustafaozhan.ccc.common.di.modules.getDatabaseModule
import com.github.mustafaozhan.ccc.common.di.modules.getSettingsModule
import com.github.mustafaozhan.logmob.initLogger
import com.github.mustafaozhan.logmob.kermit
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.context.startKoin

private const val DEFAULT_PORT = 8080
private const val REQUEST_QUEUE_LIMIT = 24
private const val RUNNING_LIMIT = 12
private val apiController: ApiController by lazy {
    koin.getDependency(ApiController::class)
}

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

    kermit.d { "BackendApp main" }

    apiController.startSyncApi()

    embeddedServer(
        factory = Netty,
        port = DEFAULT_PORT,
        configure = {
            requestQueueLimit = REQUEST_QUEUE_LIMIT
            runningLimit = RUNNING_LIMIT
        }
    ) {
        routing {
            kermit.d { "start rooting" }

            getError()
            getRoot()
            getCurrencyByName()
        }
    }.start(wait = true)
}
