package com.oztechan.ccc.config.di

import com.oztechan.ccc.config.service.update.UpdateConfigService
import com.oztechan.ccc.config.service.update.UpdateConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val configModule = module {
    singleOf(::UpdateConfigServiceImpl) { bind<UpdateConfigService>() }
}
