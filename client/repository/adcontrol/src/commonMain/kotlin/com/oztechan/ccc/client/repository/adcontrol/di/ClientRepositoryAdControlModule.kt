package com.oztechan.ccc.client.repository.adcontrol.di

import com.oztechan.ccc.client.repository.adcontrol.AdControlRepository
import com.oztechan.ccc.client.repository.adcontrol.AdControlRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientRepositoryAdControlModule = module {
    singleOf(::AdControlRepositoryImpl) { bind<AdControlRepository>() }
}
