/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.di.getForJvm
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

private val offlineRatesDao: OfflineRatesDao by lazy { app.koin.getForJvm(OfflineRatesDao::class) }

// Paths
private const val PATH_ROOT = "/"
private const val PATH_ERROR = "/error"
private const val PATH_BY_BASE = "/currency/byBase/"

// Parameter
private const val PARAMETER_BASE = "base"

// Resources
private const val INDEX_HTML = "index.html"
private const val ERROR_HTML = "error.html"

fun Application.setupRooting() = routing {
    get(PATH_ROOT) {
        call.respondText(getResource(INDEX_HTML), ContentType.Text.Html)
    }

    get(PATH_ERROR) {
        call.respondText(getResource(ERROR_HTML), ContentType.Text.Html)
    }

    get(PATH_BY_BASE) {
        call.parameters[PARAMETER_BASE]?.let { base ->
            offlineRatesDao.getOfflineCurrencyResponseByBase(base)?.let {
                call.respond(it)
            }
        }
    }
}

fun Application.getResource(source: String) =
    this::class.java.classLoader.getResource(source)?.readText() ?: ""
