package com.oztechan.ccc.backend.app.routes

import co.touchlab.kermit.Logger
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val PATH_VERSION = "/version"

internal fun Route.version(ioDispatcher: CoroutineDispatcher) {
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
