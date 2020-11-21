/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.di.initKoin
import org.koin.core.module.Module
import org.koin.dsl.module

// todo pure module should be removed when android completely moved to kmp
fun initKoin(appModule: Module? = null, pureModule: Module? = null) = initKoin(
    appModule,
    pureModule,
    platformClientModule,
    commonClientModule
)

expect val platformClientModule: Module

val commonClientModule = module {
    single { SettingsRepository(get()) }
}
