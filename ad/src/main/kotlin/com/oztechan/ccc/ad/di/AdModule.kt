package com.oztechan.ccc.ad.di

import com.oztechan.ccc.ad.AdManager
import com.oztechan.ccc.ad.AdManagerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val adModule = module {
    singleOf(::AdManagerImpl) { bind<AdManager>() }
}
