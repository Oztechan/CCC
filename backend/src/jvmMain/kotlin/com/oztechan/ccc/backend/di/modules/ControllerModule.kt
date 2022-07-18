package com.oztechan.ccc.backend.di.modules

import com.oztechan.ccc.backend.controller.ApiController
import com.oztechan.ccc.backend.controller.RootingController
import com.oztechan.ccc.common.di.DISPATCHER_IO
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

var controllerModule = module {
    single { ApiController(get(), get(), get(), get(named(DISPATCHER_IO))) }
    singleOf(::RootingController)
}
