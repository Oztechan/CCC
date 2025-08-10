package com.oztechan.ccc.backend.app.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.app.routes.currency
import com.oztechan.ccc.backend.app.routes.error
import com.oztechan.ccc.backend.app.routes.root
import com.oztechan.ccc.backend.app.routes.version
import com.oztechan.ccc.backend.controller.api.APIController
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

@Suppress("unused")
internal fun Application.apiModule() {
    Logger.v { "APIModuleKt Application.apiModule" }

    val apiController: APIController by inject()

    routing {
        root()
        currency(apiController)
        version()
        error()
    }
}
