/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.client.di

import android.content.SharedPreferences
import com.github.mustafaozhan.bookbarter.client.main.MainViewModel
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import kotlin.reflect.KClass

fun initAndroid(sharedPreferences: SharedPreferences) = initKoin(
    module {
        single<Settings> { AndroidSettings(sharedPreferences) }
    }
)

actual val platformClientModule: Module = module {
    viewModel { MainViewModel(get(), get()) }
}

fun <T> Koin.getForAndroid(clazz: KClass<*>): T =
    get(clazz, null) { parametersOf(clazz.simpleName) } as T
