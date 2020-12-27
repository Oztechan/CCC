/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend

import com.github.mustafaozhan.ccc.common.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.log.kermit
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

private val offlineRatesDao: OfflineRatesDao by lazy { app.koin.getDependency(OfflineRatesDao::class) }

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
    kermit.d { "Rooting setupRooting" }
    get(PATH_ROOT) {
        kermit.d { "GET Request  $PATH_ROOT" }
        call.respondText(getResource(INDEX_HTML), ContentType.Text.Html)
    }

    get(PATH_ERROR) {
        kermit.d { "GET Request  $ERROR_HTML" }
        call.respondText(getResource(ERROR_HTML), ContentType.Text.Html)
    }

    get(PATH_BY_BASE) {
        call.parameters[PARAMETER_BASE]?.let { base ->
            kermit.d { "GET Request  $PARAMETER_BASE $base" }
            offlineRatesDao.getOfflineCurrencyResponseByBase(base)?.let {
                call.respond(it)
            }
        } ?: run {
            kermit.d { "GET Request  $PARAMETER_BASE" }
        }
    }
}

fun Application.getResource(source: String) =
    this::class.java.classLoader.getResource(source)?.readText() ?: ""
