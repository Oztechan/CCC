package com.oztechan.ccc.backend.feature.sync.di

import com.oztechan.ccc.backend.feature.sync.SyncController
import com.oztechan.ccc.backend.feature.sync.SyncControllerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val backendControllerSyncModule = module {
    singleOf(::SyncControllerImpl) { bind<SyncController>() }
}
