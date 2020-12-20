/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.ui.main.MainViewModel
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.ui.splash.SplashViewModel
import kotlin.reflect.KClass
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.w3c.dom.Storage

fun initJS(storage: Storage) = initKoin(
    module { single { storage } }
)

actual val clientModule: Module = module {
    single { SettingsViewModel(get(), get(), get(), get()) }
    single { SplashViewModel(get()) }
    single { MainViewModel(get()) }
    single { CurrenciesViewModel(get(), get()) }
    single { CalculatorViewModel(get(), get(), get(), get()) }
    single { BarViewModel(get()) }
}

fun <T> Koin.getForJs(clazz: KClass<*>): T {
    return get(clazz, null) { parametersOf(clazz.simpleName) } as T
}
