/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.common.di.getForJvm
import com.github.mustafaozhan.ccc.common.di.initKoin
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.common.repository.PlatformRepository
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

// Configs
private const val PORT = 8000
private const val HOST = "127.0.0.1"

// Paths
private const val PATH_ROOT = "/"

// Resources
private const val INDEX_HTML = "index.html"
val app = initKoin()

private val platformRepository: PlatformRepository by lazy { app.koin.getForJvm(PlatformRepository::class) }

fun main() {
    embeddedServer(
        Netty,
        port = PORT,
        host = HOST
    ) {

        kermit.d { "Application main" }

        install(ContentNegotiation) {
            json()
        }

        routing {
            get(PATH_ROOT) {
                this::class.java.classLoader.getResource(INDEX_HTML)
                    ?.readText().let {
                        call.respondText(
                            "${platformRepository.appName}\n${platformRepository.platform.toString()}",
                            ContentType.Text.Html
                        )
                    }
            }
        }
    }.start(wait = true)
}
