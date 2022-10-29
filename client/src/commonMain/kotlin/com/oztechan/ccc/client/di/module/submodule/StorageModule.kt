package com.oztechan.ccc.client.di.module.submodule

import com.oztechan.ccc.client.storage.AppStorage
import com.oztechan.ccc.client.storage.AppStorageImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val storageModule = module {
    singleOf(::AppStorageImpl) { bind<AppStorage>() }
}
