package com.oztechan.ccc.android.core.ad.di

import com.oztechan.ccc.android.core.ad.AdManager
import com.oztechan.ccc.android.core.ad.AdManagerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidCoreAdModule = module {
    singleOf(::AdManagerImpl) { bind<AdManager>() }
}
