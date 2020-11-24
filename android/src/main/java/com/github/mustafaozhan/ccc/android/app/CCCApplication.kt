/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.github.mustafaozhan.ccc.android.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.android.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.android.ui.main.MainViewModel
import com.github.mustafaozhan.ccc.android.ui.settings.SettingsViewModel
import com.github.mustafaozhan.ccc.android.ui.splash.SplashViewModel
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.data.api.ApiFactory
import com.github.mustafaozhan.data.api.ApiRepository
import com.github.mustafaozhan.data.db.AppDatabase
import com.github.mustafaozhan.logmob.initLogMob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("unused")
class CCCApplication : MultiDexApplication() {

    companion object {
        private const val KEY_APPLICATION_PREFERENCES = "application_preferences"
    }

    override fun onCreate() {
        super.onCreate()

        initAndroid(
            getSharedPreferences(KEY_APPLICATION_PREFERENCES, Context.MODE_PRIVATE),
            module {
                single<Context> { this@CCCApplication }

                single { AppDatabase.createAppDatabase(get()) }
                single { get<AppDatabase>().currencyDao() }
                single { get<AppDatabase>().offlineRatesDao() }

                factory { ApiFactory(get()) }
                single { ApiRepository(get()) }

                viewModel { MainViewModel(get()) }
                viewModel { SettingsViewModel(get(), get(), get(), get()) }
                viewModel { CurrenciesViewModel(get(), get()) }
                viewModel { CalculatorViewModel(get(), get(), get(), get()) }
                viewModel { BarViewModel(get()) }
                viewModel { SplashViewModel(get()) }
            }
        )

        initLogMob(this, enableAnalytics = true)
    }
}
