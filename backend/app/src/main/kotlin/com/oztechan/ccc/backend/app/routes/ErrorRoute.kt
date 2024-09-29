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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val PATH_ERROR = "/error"
private const val ERROR_HTML = "error.html"

internal fun Route.error(ioDispatcher: CoroutineDispatcher) {
    get(PATH_ERROR) {
        Logger.v { "GET Request $PATH_ERROR" }

        withContext(ioDispatcher) {
            javaClass.classLoader?.getResource(ERROR_HTML)?.readText()
        }?.let { errorPage ->
            call.respondText(
                text = errorPage,
                contentType = ContentType.Text.Html,
                status = HttpStatusCode.OK
            )
        } ?: call.respond(HttpStatusCode.NotFound)
    }
}
