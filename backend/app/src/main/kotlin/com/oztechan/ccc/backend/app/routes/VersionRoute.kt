package com.oztechan.ccc.backend.app.routes

import co.touchlab.kermit.Logger
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private const val PATH_VERSION = "/version"

internal suspend fun Route.getVersion() = get(PATH_VERSION) {
    Logger.i { "GET Request $PATH_VERSION" }

    call.respondText(
        text = "Version: ${javaClass.`package`.implementationVersion}",
        contentType = ContentType.Text.Plain,
        status = HttpStatusCode.OK
    )
}
