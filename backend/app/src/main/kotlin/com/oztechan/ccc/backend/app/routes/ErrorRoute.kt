/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.app.routes

import co.touchlab.kermit.Logger
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private const val PATH_ERROR = "/error"
private const val ERROR_HTML = "error.html"

internal suspend fun Route.getError() = get(PATH_ERROR) {
    Logger.i { "GET Request $PATH_ERROR" }

    javaClass.classLoader?.getResource(ERROR_HTML)?.readText()?.let { resource ->
        call.respondText(
            text = resource,
            contentType = ContentType.Text.Html,
            status = HttpStatusCode.OK
        )
    } ?: call.respond(HttpStatusCode.NotFound)
}
