package com.oztechan.ccc.backend.app.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.app.routes.currency
import com.oztechan.ccc.backend.app.routes.error
import com.oztechan.ccc.backend.app.routes.root
import com.oztechan.ccc.backend.app.routes.version
import com.oztechan.ccc.backend.controller.api.APIController
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

@Suppress("unused")
internal fun Application.apiModule() {
    Logger.v { "APIModuleKt Application.apiModule" }

    install(ContentNegotiation) {
        json()
    }

    val apiController: APIController by inject()
    val globalScope: CoroutineScope by inject()
    val ioDispatcher: CoroutineDispatcher by inject(named(DISPATCHER_IO))

    routing {
        globalScope.launch(ioDispatcher) {
            root()
            currency(apiController)
            version()
            error()
        }
    }
}
