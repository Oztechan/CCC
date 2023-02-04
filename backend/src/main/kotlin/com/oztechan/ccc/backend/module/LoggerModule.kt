package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initLogger
import io.ktor.server.application.Application

@Suppress("unused", "UnusedReceiverParameter")
internal fun Application.loggerModule() = initLogger().also {
    Logger.i { "LoggerModuleKt Application.loggerModule" }
}
