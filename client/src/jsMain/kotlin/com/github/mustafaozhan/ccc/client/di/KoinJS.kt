/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.splash.SplashViewModel
import com.github.mustafaozhan.ccc.common.log.kermit
import org.koin.core.module.Module
import org.koin.dsl.module
import org.w3c.dom.Storage

fun initJS(storage: Storage) = initClient(
    module { single { storage } }
).also {
    kermit.d { "KoinJS initJS" }
}

actual val clientModule: Module = module {
    factory { SettingsViewModel(get(), get(), get(), get()) }
    factory { SplashViewModel(get()) }
    factory { MainViewModel(get()) }
    factory { CurrenciesViewModel(get(), get()) }
    factory { CalculatorViewModel(get(), get(), get(), get()) }
    factory { BarViewModel(get(), get()) }
}
