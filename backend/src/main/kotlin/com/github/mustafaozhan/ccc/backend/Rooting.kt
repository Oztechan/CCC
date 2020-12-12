/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

// Paths
private const val PATH_ROOT = "/"

// Resources
private const val INDEX_HTML = "index.html"

fun Application.setupRooting() = routing {
    get(PATH_ROOT) {
        call.respondText(getResource(INDEX_HTML), ContentType.Text.Html)
    }
}

fun Application.getResource(source: String) =
    this::class.java.classLoader.getResource(source)?.readText() ?: ""
