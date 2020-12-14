/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.client.ui.main.MainViewModel
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.ui.splash.SplashViewModel
import com.russhwolf.settings.JsSettings
import com.russhwolf.settings.Settings
import kotlin.reflect.KClass
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.w3c.dom.Storage

fun initJS(storage: Storage) = initKoin(
    module {
        single<Settings> { JsSettings(storage) }
    }
)

actual val platformClientModule: Module = module {
    single { SettingsViewModel(get(), get(), get(), get()) }
    single { SplashViewModel(get()) }
    single { MainViewModel(get()) }
}

fun <T> Koin.getForJs(clazz: KClass<*>): T {
    return get(clazz, null) { parametersOf(clazz.simpleName) } as T
}
