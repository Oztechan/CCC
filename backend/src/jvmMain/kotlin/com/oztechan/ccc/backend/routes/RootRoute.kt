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

private const val PATH_ROOT = "/"
private const val INDEX_HTML = "index.html"

suspend fun Route.getRoot() = get(PATH_ROOT) {
    Logger.i { "GET Request $PATH_ROOT" }
    call.respondText(
        javaClass.classLoader?.getResourceByName(INDEX_HTML) ?: "",
        ContentType.Text.Html
    )
}
