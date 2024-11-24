package com.oztechan.ccc.backend.app.routes

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

private const val PATH_VERSION = "/version"

internal fun Route.version() {
    val ioDispatcher: CoroutineDispatcher by inject(named(DISPATCHER_IO))

    get(PATH_VERSION) {
        Logger.v { "GET Request $PATH_VERSION" }

        withContext(ioDispatcher) {
            javaClass.`package`?.implementationVersion
        }?.let { version ->
            call.respondText(
                text = "Version: $version",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.OK
            )
        } ?: call.respond(HttpStatusCode.NotFound)
    }
}
