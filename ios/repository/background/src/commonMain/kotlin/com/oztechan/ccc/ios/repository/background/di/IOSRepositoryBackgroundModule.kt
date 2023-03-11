package com.oztechan.ccc.ios.repository.background.di

import com.oztechan.ccc.ios.repository.background.BackgroundRepository
import com.oztechan.ccc.ios.repository.background.BackgroundRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val iosRepositoryBackgroundModule = module {
    singleOf(::BackgroundRepositoryImpl) { bind<BackgroundRepository>() }
}
