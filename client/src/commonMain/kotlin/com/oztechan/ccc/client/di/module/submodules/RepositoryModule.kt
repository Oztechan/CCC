package com.oztechan.ccc.client.di.module.submodules

import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.ad.AdRepositoryImpl
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepositoryImpl
import com.oztechan.ccc.client.repository.background.BackgroundRepository
import com.oztechan.ccc.client.repository.background.BackgroundRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val repositoryModule = module {
    single<AppConfigRepository> { AppConfigRepositoryImpl(get(), get(), provideDevice()) }
    single<AdRepository> { AdRepositoryImpl(get(), get(), provideDevice()) }
    singleOf(::AppConfigRepositoryImpl) { bind<AppConfigRepository>() }
    singleOf(::BackgroundRepositoryImpl) { bind<BackgroundRepository>() }
}

internal expect fun provideDevice(): Device
