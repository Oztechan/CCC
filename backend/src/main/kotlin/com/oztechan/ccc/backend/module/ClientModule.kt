package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.controller.client.ClientController
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import io.ktor.server.application.Application
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import kotlin.time.Duration.Companion.hours

@Suppress("unused")
internal fun Application.clientModule() {
    Logger.i { "ClientModuleKt Application.clientModule" }

    val clientController: ClientController by inject()
    val globalScope: CoroutineScope by inject()
    val ioDispatcher: CoroutineDispatcher by inject(named(DISPATCHER_IO))

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            clientController.syncPopularCurrencies()
            delay(1.hours.inWholeMilliseconds)
        }
    }

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            clientController.syncUnPopularCurrencies()
            delay(8.hours.inWholeMilliseconds)
        }
    }
}
