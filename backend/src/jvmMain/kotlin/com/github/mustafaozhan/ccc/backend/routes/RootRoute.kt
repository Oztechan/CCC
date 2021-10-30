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

private const val PATH_ROOT = "/"
private const val INDEX_HTML = "index.html"

suspend fun Route.getRoot() = get(PATH_ROOT) {
    Logger.i { "GET Request $PATH_ROOT" }
    call.respondText(
        javaClass.classLoader.getResourceByName(INDEX_HTML),
        ContentType.Text.Html
    )
}
