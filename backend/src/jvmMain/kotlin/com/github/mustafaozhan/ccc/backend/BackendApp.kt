/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import co.touchlab.kermit.Logger
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
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin

private const val DEFAULT_PORT = 8080
private const val REQUEST_QUEUE_LIMIT = 32
private const val RUNNING_LIMIT = 16
private val apiController: ApiController by lazy {
    koin.getDependency(ApiController::class)
}

fun main() {
    initLogger().let {
        it.i { "Logger initialized" }
    }

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

    Logger.i { "BackendApp main" }

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
            Logger.i { "start rooting" }

            CoroutineScope(Dispatchers.IO).launch {
                getError()
                getRoot()
                getCurrencyByName()
            }
        }
    }.start(wait = true)
}
