/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.ui.main.MainViewModel
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.ui.splash.SplashViewModel
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initIOS(userDefaults: NSUserDefaults) = initKoin(
    module {
        // todo fix AppleSettings
        single<Settings> { AppleSettings(userDefaults) }
    }
)

actual val platformClientModule: Module = module {
    single { SettingsViewModel(get(), get(), get(), get()) }
    single { SplashViewModel(get()) }
    single { MainViewModel(get()) }
    single { CurrenciesViewModel(get(), get()) }
}

fun Koin.getForIOS(objCClass: ObjCClass): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null) { parametersOf(objCClass::class.simpleName) }
}
