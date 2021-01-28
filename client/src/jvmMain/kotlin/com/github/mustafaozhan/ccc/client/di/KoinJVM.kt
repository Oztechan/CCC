/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.viewmodel.BarViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.MainViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.SettingsViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.SplashViewModel
import com.github.mustafaozhan.ccc.common.log.kermit
import java.util.prefs.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module

fun initJVM(delegate: Preferences) = initClient(
    module { single { delegate } }
).also {
    kermit.d { "KoinJVM initJVM" }
}

actual val clientModule: Module = module {
    factory { SettingsViewModel(get(), get(), get(), get()) }
    factory { SplashViewModel(get()) }
    factory { MainViewModel(get()) }
    factory { CurrenciesViewModel(get(), get()) }
    factory { CalculatorViewModel(get(), get(), get(), get()) }
    factory { BarViewModel(get(), get()) }
}
