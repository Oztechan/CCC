/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.mustafaozhan.ccc.android.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.android.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.android.ui.main.MainViewModel
import com.github.mustafaozhan.ccc.android.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.android.ui.splash.SplashViewModel
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.logmob.initLogMob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("unused")
class CCCApplication : Application() {
    companion object {
        private const val KEY_APPLICATION_PREFERENCES = "application_preferences"
    }

    private val androidModule = module {
        module {
            single<Context> { this@CCCApplication }
            single<SharedPreferences> {
                this@CCCApplication.getSharedPreferences(
                    KEY_APPLICATION_PREFERENCES,
                    Context.MODE_PRIVATE
                )
            }

            viewModel { BarViewModel(get()) }
            viewModel { CalculatorViewModel(get()) }
            viewModel { CurrenciesViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { SettingsViewModel(get()) }
            viewModel { SplashViewModel(get()) }
        }
    }

    override fun onCreate() {
        super.onCreate()

        initAndroid(androidModule)
        initLogMob(this, enableAnalytics = true)
    }
}
