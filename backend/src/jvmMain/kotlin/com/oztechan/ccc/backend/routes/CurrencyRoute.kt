/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.routes

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.controller.RootingController
import com.oztechan.ccc.backend.di.koin
import com.oztechan.ccc.common.di.getDependency
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

private const val PATH_BY_BASE = "/currency/byBase/"
private const val PARAMETER_BASE = "base"

suspend fun Route.getCurrencyByName(
    rootingController: RootingController = koin.getDependency(RootingController::class)
) = get(PATH_BY_BASE) {
    call.parameters[PARAMETER_BASE]?.let { base ->
        Logger.i { "GET Request $PARAMETER_BASE $base" }
        rootingController.getOfflineCurrencyResponseByBase(base)?.let {
            call.respond(it)
        }
    } ?: run {
        Logger.i { "GET Request  $PARAMETER_BASE" }
    }
}
