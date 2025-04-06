package com.oztechan.ccc.backend.app.module

import co.touchlab.kermit.Logger
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

@Suppress("unused")
internal fun Application.ktorModule() {
    Logger.v { "APIModuleKt Application.ktorModule" }

    install(ContentNegotiation) {
        json()
    }
}