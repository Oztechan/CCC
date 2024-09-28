/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.app.routes

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.controller.api.APIController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private const val PATH_BY_BASE = "/currency/byBase/"
private const val PARAMETER_BASE = "base"

internal fun Route.currency(
    apiController: APIController
) {
    get(PATH_BY_BASE) {
        Logger.v { "GET Request $PATH_BY_BASE" }

        call.parameters[PARAMETER_BASE]?.let { base ->
            Logger.v { "Parameter: $PARAMETER_BASE $base" }

            apiController.getExchangeRateByBase(base)
                ?.let { call.respond(HttpStatusCode.OK, it) }
                ?: call.respond(HttpStatusCode.NotFound)
        } ?: call.respond(HttpStatusCode.BadRequest)
    }
}
