/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.backend.di.Koin
import com.github.mustafaozhan.ccc.common.log.kermit
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
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

        install(ContentNegotiation) {
            json()
        }

        apiController.startSyncApi()

        setupRooting(rootingController)

    }.start(wait = true)
}
