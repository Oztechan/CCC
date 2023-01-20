package com.oztechan.ccc.backend.di

import com.oztechan.ccc.backend.controller.client.ClientController
import com.oztechan.ccc.backend.controller.client.ClientControllerImpl
import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val backendModule = module {
    singleOf(::ClientControllerImpl) { bind<ClientController>() }
    singleOf(::ServerControllerImpl) { bind<ServerController>() }
}
