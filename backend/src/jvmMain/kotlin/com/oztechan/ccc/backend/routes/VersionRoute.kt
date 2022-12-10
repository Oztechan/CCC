package com.oztechan.ccc.backend.routes

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.BuildKonfig
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

private const val PATH_VERSION = "/version"

internal suspend fun Route.getVersion() = get(PATH_VERSION) {
    Logger.i { "GET Request Version" }
    call.respondText(
        """
        Version Name: ${BuildKonfig.versionName}
        Version Code: ${BuildKonfig.versionCode}
        """.trimIndent()
    )
}
