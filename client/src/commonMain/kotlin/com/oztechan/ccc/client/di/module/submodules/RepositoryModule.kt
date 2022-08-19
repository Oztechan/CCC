package com.oztechan.ccc.client.di.module.submodules

import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.background.BackgroundRepository
import com.oztechan.ccc.client.repository.background.BackgroundRepositoryImpl
import com.oztechan.ccc.client.repository.session.SessionRepository
import com.oztechan.ccc.client.repository.session.SessionRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val repositoryModule = module {
    single<SessionRepository> { SessionRepositoryImpl(get(), get(), provideDevice()) }
    singleOf(::SessionRepositoryImpl) { bind<SessionRepository>() }
    singleOf(::BackgroundRepositoryImpl) { bind<BackgroundRepository>() }
}

internal expect fun provideDevice(): Device
