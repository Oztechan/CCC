/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.common.di.initKoin
import com.github.mustafaozhan.ccc.common.kermit
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

// Configs
private const val PORT = 8080
private const val HOST = "127.0.0.1"

val app = initKoin()

fun main() {
    embeddedServer(
        Netty,
        port = PORT,
        host = HOST
    ) {

        kermit.d { "App initialised" }

        install(ContentNegotiation) {
            json()
        }

        checkApi()

        setupRooting()

    }.start(wait = true)
}
