package com.oztechan.ccc.backend.di.modules

import com.oztechan.ccc.backend.controller.ApiController
import com.oztechan.ccc.backend.controller.RootingController
import org.koin.dsl.module

var controllerModule = module {
    single { ApiController(get(), get()) }
    single { RootingController(get()) }
}
