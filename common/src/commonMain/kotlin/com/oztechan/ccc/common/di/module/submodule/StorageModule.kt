package com.oztechan.ccc.common.di.module.submodule

import com.oztechan.ccc.common.storage.AppStorage
import com.oztechan.ccc.common.storage.AppStorageImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val storageModule = module {
    singleOf(::AppStorageImpl) { bind<AppStorage>() }
}
