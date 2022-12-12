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
import org.koin.java.KoinJavaComponent.inject

private const val NUMBER_OF_REFRESH_IN_A_DAY_POPULAR = 24
private const val NUMBER_OF_REFRESH_IN_A_DAY_UN_POPULAR = 3

@Suppress("unused", "UnusedReceiverParameter")
internal fun Application.clientModule() {
    Logger.i { "ClientModuleKt Application.clientModule" }

    val clientController: ClientController by inject(ClientController::class.java)
    val globalScope: CoroutineScope by inject(CoroutineScope::class.java)
    val ioDispatcher: CoroutineDispatcher by inject(CoroutineDispatcher::class.java, named(DISPATCHER_IO))

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
