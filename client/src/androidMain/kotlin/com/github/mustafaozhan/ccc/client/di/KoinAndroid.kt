/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import android.content.Context
import android.content.SharedPreferences
import com.github.mustafaozhan.ccc.client.viewmodel.bar.BarViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.client.viewmodel.splash.SplashViewModel
import com.github.mustafaozhan.ccc.common.log.kermit
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

fun initAndroid(context: Context): KoinApplication = initClient(
    module {
        single { context }
        single<SharedPreferences> {
            context.getSharedPreferences(
                KEY_APPLICATION_PREFERENCES,
                Context.MODE_PRIVATE
            )
        }
    }
).also {
    kermit.d { "KoinAndroid initAndroid" }
}

actual val clientModule: Module = module {
    viewModel { SettingsViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { CurrenciesViewModel(get(), get()) }
    viewModel { CalculatorViewModel(get(), get(), get(), get()) }
    viewModel { BarViewModel(get()) }
}
