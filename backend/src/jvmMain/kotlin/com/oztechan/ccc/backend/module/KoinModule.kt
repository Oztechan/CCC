package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.di.initKoin
import io.ktor.server.application.Application

@Suppress("unused", "UnusedReceiverParameter")
internal fun Application.koinModule() {
    Logger.i { "KoinModuleKt Application.koinModule" }

    initKoin()
}
