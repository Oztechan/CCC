/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initLogger
import com.oztechan.ccc.backend.di.initKoin
import com.oztechan.ccc.backend.repository.api.ApiRepository
import com.oztechan.ccc.backend.routes.getCurrencyByName
import com.oztechan.ccc.backend.routes.getError
import com.oztechan.ccc.backend.routes.getRoot
import com.oztechan.ccc.common.di.DISPATCHER_IO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

private const val DEFAULT_PORT = 8080
private const val REQUEST_QUEUE_LIMIT = 48
private const val RUNNING_LIMIT = 30

private val apiController: ApiRepository by inject(ApiRepository::class.java)
private val ioDispatcher: CoroutineDispatcher by inject(CoroutineDispatcher::class.java, named(DISPATCHER_IO))
private val globalScope: CoroutineScope by inject(CoroutineScope::class.java)

fun main() {
    initLogger()

    Logger.i { "Application main" }

    initKoin()

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

            globalScope.launch(ioDispatcher) {
                getError()
                getRoot()
                getCurrencyByName(apiController)
            }
        }
    }.start(wait = true)
}
