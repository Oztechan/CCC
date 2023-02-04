package com.oztechan.ccc.backend.controller.api.di

import com.oztechan.ccc.backend.controller.api.APIController
import com.oztechan.ccc.backend.controller.api.APIControllerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val backendControllerAPIModule = module {
    singleOf(::APIControllerImpl) { bind<APIController>() }
}
