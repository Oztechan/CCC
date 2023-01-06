package com.oztechan.ccc.backend.di.module.submodule

import com.oztechan.ccc.backend.controller.client.ClientController
import com.oztechan.ccc.backend.controller.client.ClientControllerImpl
import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val controllerModule = module {
    singleOf(::ClientControllerImpl) { bind<ClientController>() }
    singleOf(::ServerControllerImpl) { bind<ServerController>() }
}
