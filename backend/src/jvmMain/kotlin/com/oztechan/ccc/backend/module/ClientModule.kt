package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.controller.client.ClientController
import com.oztechan.ccc.common.di.DISPATCHER_IO
import com.oztechan.ccc.common.util.DAY
import io.ktor.server.application.Application
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

private const val NUMBER_OF_REFRESH_IN_A_DAY_POPULAR = 24
private const val NUMBER_OF_REFRESH_IN_A_DAY_UN_POPULAR = 3

@Suppress("unused")
internal fun Application.clientModule() {
    Logger.i { "ClientModuleKt Application.clientModule" }

    val clientController: ClientController by inject()
    val globalScope: CoroutineScope by inject()
    val ioDispatcher: CoroutineDispatcher by inject(named(DISPATCHER_IO))

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            clientController.syncPopularCurrencies()
            delay(DAY / NUMBER_OF_REFRESH_IN_A_DAY_POPULAR)
        }
    }

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            clientController.syncUnPopularCurrencies()
            delay(DAY / NUMBER_OF_REFRESH_IN_A_DAY_UN_POPULAR)
        }
    }
}
