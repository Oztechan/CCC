package com.oztechan.ccc.backend.di.module.submodule

import com.oztechan.ccc.backend.controller.server.ServerController
import com.oztechan.ccc.backend.controller.server.ServerControllerImpl
import com.oztechan.ccc.backend.repository.api.ApiRepository
import com.oztechan.ccc.backend.repository.api.ApiRepositoryImpl
import com.oztechan.ccc.common.di.DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val controllerModule = module {
    single<ApiRepository> { ApiRepositoryImpl(get(), get(), get(), get(), get(named(DISPATCHER_IO))) }
    single<ServerController> { ServerControllerImpl(get()) }
}
