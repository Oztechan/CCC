package com.oztechan.ccc.backend.di

import co.touchlab.kermit.Logger
import com.oztechan.ccc.backend.di.module.backendModules
import com.oztechan.ccc.common.di.module.commonModules
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(
        buildList {
            addAll(backendModules)
            addAll(commonModules)
        }
    )
}.also {
    Logger.i { "Koin initialised" }
}
