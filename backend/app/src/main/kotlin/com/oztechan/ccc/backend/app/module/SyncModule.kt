package com.oztechan.ccc.backend.app.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.controller.sync.SyncController
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
internal fun Application.syncModule() {
    Logger.v { "SyncModuleKt Application.syncModule" }

    val syncController: SyncController by inject()
    val globalScope: CoroutineScope by inject()
    val ioDispatcher: CoroutineDispatcher by inject(named(DISPATCHER_IO))

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            syncController.syncPrimaryCurrencies()
            delay(1.hours.inWholeMilliseconds)
        }
    }

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            syncController.syncSecondaryCurrencies()
            delay(2.hours.inWholeMilliseconds)
        }
    }

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            syncController.syncTertiaryCurrencies()
            delay(3.hours.inWholeMilliseconds)
        }
    }

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            syncController.syncUnPopularCurrencies()
            delay(24.hours.inWholeMilliseconds)
        }
    }
}
