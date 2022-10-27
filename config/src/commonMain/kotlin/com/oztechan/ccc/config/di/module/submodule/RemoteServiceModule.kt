package com.oztechan.ccc.config.di.module.submodule

import com.oztechan.ccc.config.ConfigService
import com.oztechan.ccc.config.ConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val remoteServiceModule = module {
    singleOf(::ConfigServiceImpl) { bind<ConfigService>() }
}
