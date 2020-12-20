/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.ui.main.MainViewModel
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.ui.splash.SplashViewModel
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initIOS(userDefaults: NSUserDefaults) = initKoin(
    module { single { userDefaults } }
)

actual val clientModule: Module = module {
    single { SettingsViewModel(get(), get(), get(), get()) }
    single { SplashViewModel(get()) }
    single { MainViewModel(get()) }
    single { CurrenciesViewModel(get(), get()) }
    single { CalculatorViewModel(get(), get(), get(), get()) }
    single { BarViewModel(get()) }
}

fun Koin.getForIOS(objCClass: ObjCClass): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null) { parametersOf(objCClass::class.simpleName) }
}
