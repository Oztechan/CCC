package com.oztechan.ccc.config.di.module.submodule

import com.oztechan.ccc.config.AppConfigService
import com.oztechan.ccc.config.AppConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val configServiceModule = module {
    singleOf(::AppConfigServiceImpl) { bind<AppConfigService>() }
}
