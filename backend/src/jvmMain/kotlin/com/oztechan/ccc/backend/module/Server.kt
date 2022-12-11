package com.oztechan.ccc.backend.module

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.repository.api.ApiRepository
import com.oztechan.ccc.backend.routes.getCurrencyByBase
import com.oztechan.ccc.backend.routes.getError
import com.oztechan.ccc.backend.routes.getRoot
import com.oztechan.ccc.backend.routes.getVersion
import com.oztechan.ccc.common.di.DISPATCHER_IO
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

@Suppress("unused")
internal fun Application.server() {
    val apiController: ApiRepository by inject(ApiRepository::class.java)
    val globalScope: CoroutineScope by inject(CoroutineScope::class.java)
    val ioDispatcher: CoroutineDispatcher by inject(CoroutineDispatcher::class.java, named(DISPATCHER_IO))

    routing {
        Logger.i { "start rooting" }

        globalScope.launch(ioDispatcher) {
            getError()
            getRoot()
            getCurrencyByBase(apiController)
            getVersion()
        }
    }
}
