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
import com.github.mustafaozhan.ccc.android.ui.splash.SplashViewModel
import com.github.mustafaozhan.ccc.client.di.initAndroid
import com.github.mustafaozhan.logmob.initLogMob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@Suppress("unused")
class CCCApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initAndroid(
            module {
                single<Context> { this@CCCApplication }

                viewModel { MainViewModel(get()) }
                viewModel { CurrenciesViewModel(get(), get()) }
                viewModel { CalculatorViewModel(get(), get(), get(), get()) }
                viewModel { BarViewModel(get()) }
                viewModel { SplashViewModel(get()) }
            }
        )

        initLogMob(this, enableAnalytics = true)
    }
}
