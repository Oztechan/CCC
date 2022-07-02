/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.routes

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.util.getResourceByName
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private const val PATH_ERROR = "/error"
private const val ERROR_HTML = "error.html"

suspend fun Route.getError() = get(PATH_ERROR) {
    Logger.i { "GET Request $ERROR_HTML" }
    call.respondText(
        javaClass.classLoader?.getResourceByName(ERROR_HTML) ?: "",
        ContentType.Text.Html
    )
}
