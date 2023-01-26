package com.oztechan.ccc.client.repository.ad.di

import com.oztechan.ccc.client.repository.ad.AdRepository
import com.oztechan.ccc.client.repository.ad.AdRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientRepositoryAdModule = module {
    singleOf(::AdRepositoryImpl) { bind<AdRepository>() }
}
