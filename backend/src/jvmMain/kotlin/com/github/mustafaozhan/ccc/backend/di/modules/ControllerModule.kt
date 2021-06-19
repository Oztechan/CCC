package com.github.mustafaozhan.ccc.backend.di.modules

import com.github.mustafaozhan.ccc.backend.controller.ApiController
import com.github.mustafaozhan.ccc.backend.controller.RootingController
import org.koin.dsl.module

var controllerModule = module {
    single { ApiController(get(), get()) }
    single { RootingController(get()) }
}
