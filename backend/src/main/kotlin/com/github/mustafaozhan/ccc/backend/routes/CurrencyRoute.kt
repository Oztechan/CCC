/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.routes

import com.github.mustafaozhan.ccc.backend.controller.RootingController
import com.github.mustafaozhan.logmob.kermit
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

private const val PATH_BY_BASE = "/currency/byBase/"
private const val PARAMETER_BASE = "base"

fun Route.getCurrencyByName(rootingController: RootingController) = get(PATH_BY_BASE) {
    call.parameters[PARAMETER_BASE]?.let { base ->
        kermit.d { "GET Request  $PARAMETER_BASE $base" }
        rootingController.getOfflineCurrencyResponseByBase(base)?.let {
            call.respond(it)
        }
    } ?: run {
        kermit.d { "GET Request  $PARAMETER_BASE" }
    }
}