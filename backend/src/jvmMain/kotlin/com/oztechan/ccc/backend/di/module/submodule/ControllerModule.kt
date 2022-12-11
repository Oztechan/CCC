package com.oztechan.ccc.backend.di.module.submodule

import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import org.koin.dsl.module

internal val controllerModule = module {
    single<ServerController> { ServerControllerImpl(get()) }
}
