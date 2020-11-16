/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.common.di

import co.touchlab.kermit.CommonLogger
import co.touchlab.kermit.Kermit
import com.github.mustafaozhan.bookbarter.common.repository.PlatformRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(vararg modules: Module?) = startKoin {
    kermit.d { "Koin initKoin" }
    modules.forEach { it?.let { modules(it) } }
    modules(commonModule)
}

val kermit = Kermit(CommonLogger()).apply { d { "Kermit initialised" } }

var commonModule: Module = module {
    single { PlatformRepository() }
    factory { kermit }
}
