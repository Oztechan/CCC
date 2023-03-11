package com.oztechan.ccc.client.configservice.ad.di

import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.ad.AdConfigServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientConfigServiceAdModule = module {
    singleOf(::AdConfigServiceImpl) { bind<AdConfigService>() }
}
