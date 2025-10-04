package com.oztechan.ccc.backend.app.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.app.util.isProduction
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
import kotlin.time.Duration.Companion.seconds

@Suppress("unused")
internal fun Application.syncModule() {
    Logger.v { "SyncModuleKt Application.syncModule" }

    val syncController: SyncController by inject()
    val globalScope: CoroutineScope by inject()
    val ioDispatcher: CoroutineDispatcher by inject(named(DISPATCHER_IO))

    val delayDuration = if (isProduction) 5.seconds else 1.hours

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            syncController.syncPrimaryCurrencies(delayDuration)
            delay(1.hours.inWholeMilliseconds)
        }
    }

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            syncController.syncSecondaryCurrencies(delayDuration)
            delay(2.hours.inWholeMilliseconds)
        }
    }

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            syncController.syncTertiaryCurrencies(delayDuration)
            delay(3.hours.inWholeMilliseconds)
        }
    }

    globalScope.launch(ioDispatcher) {
        while (isActive) {
            syncController.syncUnPopularCurrencies(delayDuration)
            delay(12.hours.inWholeMilliseconds)
        }
    }
}
