package com.oztechan.ccc.client.repository.background.di

import com.oztechan.ccc.client.repository.background.BackgroundRepository
import com.oztechan.ccc.client.repository.background.BackgroundRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientRepositoryBackgroundModule = module {
    singleOf(::BackgroundRepositoryImpl) { bind<BackgroundRepository>() }
}
