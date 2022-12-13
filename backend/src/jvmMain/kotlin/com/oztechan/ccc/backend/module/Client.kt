package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.controller.client.ClientController
import io.ktor.server.application.Application
import org.koin.java.KoinJavaComponent.inject

@Suppress("unused", "UnusedReceiverParameter")
internal fun Application.client() {
    Logger.i { "Application client" }

    val clientController: ClientController by inject(ClientController::class.java)

    clientController.startSyncApi()
}
