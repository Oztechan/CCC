/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import android.content.Context
import android.content.SharedPreferences
import com.github.mustafaozhan.ccc.client.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.ui.main.MainViewModel
import com.github.mustafaozhan.ccc.client.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.ui.splash.SplashViewModel
import kotlin.reflect.KClass
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

fun initAndroid(context: Context) = initKoin(
    module { single { context } }
)

actual val clientModule: Module = module {

    single<SharedPreferences> {
        get<Context>().getSharedPreferences(
            KEY_APPLICATION_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    viewModel { SettingsViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { CurrenciesViewModel(get(), get()) }
    viewModel { CalculatorViewModel(get(), get(), get(), get()) }
    viewModel { BarViewModel(get()) }
}

fun <T> Koin.getForAndroid(clazz: KClass<*>): T =
    get(clazz, null) { parametersOf(clazz.simpleName) } as T
