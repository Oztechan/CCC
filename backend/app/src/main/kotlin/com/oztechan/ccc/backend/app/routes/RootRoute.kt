/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

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

private const val PATH_ROOT = "/"
private const val INDEX_HTML = "index.html"

internal fun Route.root() {
    val ioDispatcher: CoroutineDispatcher by inject(named(DISPATCHER_IO))

    get(PATH_ROOT) {
        Logger.v { "GET Request $PATH_ROOT" }

        withContext(ioDispatcher) {
            javaClass.classLoader?.getResource(INDEX_HTML)?.readText()
        }?.let { rootPage ->
            call.respondText(
                text = rootPage,
                contentType = ContentType.Text.Html,
                status = HttpStatusCode.OK
            )
        } ?: call.respond(HttpStatusCode.NotFound)
    }
}
