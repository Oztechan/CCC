/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.routes

import co.touchlab.kermit.Logger
import com.github.mustafaozhan.ccc.backend.util.getResourceByName
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get

private const val PATH_ERROR = "/error"
private const val ERROR_HTML = "error.html"

suspend fun Route.getError() = get(PATH_ERROR) {
    Logger.i { "GET Request $ERROR_HTML" }
    call.respondText(
        javaClass.classLoader.getResourceByName(ERROR_HTML),
        ContentType.Text.Html
    )
}
