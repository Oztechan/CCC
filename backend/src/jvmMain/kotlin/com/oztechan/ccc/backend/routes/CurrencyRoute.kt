/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.routes

import co.touchlab.kermit.Logger
import io.ktor.http.HttpStatusCode
import com.oztechan.ccc.backend.repository.api.ApiRepository
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private const val PATH_BY_BASE = "/currency/byBase/"
private const val PARAMETER_BASE = "base"

suspend fun Route.getCurrencyByName(apiController: ApiRepository) = get(PATH_BY_BASE) {
    call.parameters[PARAMETER_BASE]?.let { base ->
        Logger.i { "GET Request $PARAMETER_BASE $base" }
        apiController.getOfflineCurrencyResponseByBase(base)?.let {
            call.respond(it)
        } ?: call.respond(HttpStatusCode.NotFound)
    } ?: run {
        Logger.i { "GET Request  $PARAMETER_BASE" }
        call.respond(HttpStatusCode.BadRequest)
    }
}
