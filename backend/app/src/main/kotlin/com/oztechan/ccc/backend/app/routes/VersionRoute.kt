package com.oztechan.ccc.backend.app.routes

import co.touchlab.kermit.Logger
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private const val PATH_VERSION = "/version"

internal fun Route.version() = get(PATH_VERSION) {
    Logger.v { "GET Request $PATH_VERSION" }

    javaClass.`package`
        ?.implementationVersion
        ?.let { version ->
            call.respondText(
                text = "Version: $version",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.OK
            )
        } ?: call.respond(HttpStatusCode.NotFound)
}
