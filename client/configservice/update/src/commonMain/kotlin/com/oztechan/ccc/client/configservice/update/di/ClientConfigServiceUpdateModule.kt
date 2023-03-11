package com.oztechan.ccc.client.configservice.update.di

import com.oztechan.ccc.client.configservice.update.UpdateConfigService
import com.oztechan.ccc.client.configservice.update.UpdateConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientConfigServiceUpdateModule = module {
    singleOf(::UpdateConfigServiceImpl) { bind<UpdateConfigService>() }
}
