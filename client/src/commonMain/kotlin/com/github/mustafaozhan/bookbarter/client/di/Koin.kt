/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.client.di

import com.github.mustafaozhan.bookbarter.client.repo.SettingsRepository
import com.github.mustafaozhan.bookbarter.common.di.initKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(appModule: Module? = null) = initKoin(
    appModule,
    platformClientModule,
    commonClientModule
)

expect val platformClientModule: Module

val commonClientModule = module {
    single { SettingsRepository(get()) }
}
