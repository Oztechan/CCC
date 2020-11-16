/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.backend

import com.github.mustafaozhan.bookbarter.common.di.getForJvm
import com.github.mustafaozhan.bookbarter.common.di.initKoin
import com.github.mustafaozhan.bookbarter.common.repository.PlatformRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

// Configs
private const val PORT = 8000
private const val HOST = "127.0.0.1"

// Paths
private const val PATH_ROOT = "/"

// Resources
private const val INDEX_HTML = "index.html"
val app = initKoin()

fun main() {
    embeddedServer(
        Netty,
        port = PORT,
        host = HOST
    ) {

        install(ContentNegotiation) {
            json()
        }

        routing {
            get(PATH_ROOT) {
                this::class.java.classLoader.getResource(INDEX_HTML)
                    ?.readText().let {
                        call.respondText(
                            "CCC Backend",
                            ContentType.Text.Html
                        )
                    }
            }
        }
    }.start(wait = true)
}

