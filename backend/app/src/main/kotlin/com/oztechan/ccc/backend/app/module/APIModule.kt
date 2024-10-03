package com.oztechan.ccc.backend.app.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.app.routes.currency
import com.oztechan.ccc.backend.app.routes.error
import com.oztechan.ccc.backend.app.routes.root
import com.oztechan.ccc.backend.app.routes.version
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

@Suppress("unused")
internal fun Application.apiModule() {
    Logger.v { "APIModuleKt Application.apiModule" }

    install(ContentNegotiation) {
        json()
    }

    routing {
        root()
        currency()
        version()
        error()
    }
}
