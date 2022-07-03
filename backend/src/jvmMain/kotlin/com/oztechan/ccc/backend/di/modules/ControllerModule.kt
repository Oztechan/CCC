package com.oztechan.ccc.backend.di.modules

import com.oztechan.ccc.backend.controller.ApiController
import com.oztechan.ccc.backend.controller.RootingController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

var controllerModule = module {
    singleOf(::ApiController)
    singleOf(::RootingController)
}
