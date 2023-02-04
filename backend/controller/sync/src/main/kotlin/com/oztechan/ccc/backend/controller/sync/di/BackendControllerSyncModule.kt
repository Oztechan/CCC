package com.oztechan.ccc.backend.controller.sync.di

import com.oztechan.ccc.backend.controller.sync.SyncController
import com.oztechan.ccc.backend.controller.sync.SyncControllerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val backendControllerSyncModule = module {
    singleOf(::SyncControllerImpl) { bind<SyncController>() }
}
