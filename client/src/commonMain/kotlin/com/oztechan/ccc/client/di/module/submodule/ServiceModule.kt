package com.oztechan.ccc.client.di.module.submodule

import com.oztechan.ccc.config.ConfigService
import com.oztechan.ccc.config.ConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val serviceModule = module {
    singleOf(::ConfigServiceImpl) { bind<ConfigService>() }
}
