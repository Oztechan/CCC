package com.oztechan.ccc.client.repository.appconfig.di

import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientRepositoryAppConfigModule = module {
    singleOf(::AppConfigRepositoryImpl) { bind<AppConfigRepository>() }
}
