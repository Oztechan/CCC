package com.oztechan.ccc.client.storage.app.di

import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.client.storage.app.AppStorageImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val clientStorageAppModule = module {
    singleOf(::AppStorageImpl) { bind<AppStorage>() }
}
