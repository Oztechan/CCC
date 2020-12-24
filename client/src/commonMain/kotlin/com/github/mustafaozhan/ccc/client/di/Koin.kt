/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.ui.bar.BarUseCase
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorUseCase
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesUseCase
import com.github.mustafaozhan.ccc.client.ui.main.MainUseCase
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsUseCase
import com.github.mustafaozhan.ccc.client.ui.splash.SplashUseCase
import com.github.mustafaozhan.ccc.common.di.initCommon
import com.github.mustafaozhan.ccc.common.log.kermit
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

fun initClient(appModule: Module, useFakes: Boolean = false): KoinApplication = initCommon(
    appModule.plus(clientModule), useFakes
).also {
    kermit.d { "Koin initClient" }
}

private val clientModule: Module = module {
    single { SettingsUseCase(get(), get(), get(), get()) }
    single { SplashUseCase(get()) }
    single { MainUseCase(get()) }
    single { CurrenciesUseCase(get(), get()) }
    single { CalculatorUseCase(get(), get(), get(), get()) }
    single { BarUseCase(get()) }
}
