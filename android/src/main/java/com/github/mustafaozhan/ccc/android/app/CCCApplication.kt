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
import com.github.mustafaozhan.ccc.client.di.initKoin
import com.github.mustafaozhan.data.api.ApiFactory
import com.github.mustafaozhan.data.api.ApiRepository
import com.github.mustafaozhan.data.db.AppDatabase
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.logmob.initLogMob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class CCCApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initKoin(
            module {
                single<Context> { this@CCCApplication }

                single { PreferencesRepository(get()) }

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
                viewModel { MainViewModel(get()) }
            }
        )

        initLogMob(this, enableAnalytics = true)
    }
}
