/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.di

import com.github.mustafaozhan.ccc.android.ui.bar.BarViewModel
import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorViewModel
import com.github.mustafaozhan.ccc.android.ui.currencies.CurrenciesViewModel
import com.github.mustafaozhan.ccc.android.ui.main.MainViewModel
import com.github.mustafaozhan.ccc.android.ui.settings.SettingsViewModel
import com.github.mustafaozhan.data.api.ApiRepository
import com.github.mustafaozhan.data.db.CurrencyDao
import com.github.mustafaozhan.data.db.OfflineRatesDao
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    internal fun providesMainViewModel(
        preferencesRepository: PreferencesRepository
    ) = MainViewModel(preferencesRepository)

    @Provides
    @ActivityScope
    internal fun providesCalculatorViewModel(
        preferencesRepository: PreferencesRepository,
        apiRepository: ApiRepository,
        currencyDao: CurrencyDao,
        offlineRatesDao: OfflineRatesDao
    ) = CalculatorViewModel(
        preferencesRepository,
        apiRepository,
        currencyDao,
        offlineRatesDao
    )

    @Provides
    @ActivityScope
    internal fun providesSettingsViewModel(
        preferencesRepository: PreferencesRepository,
        apiRepository: ApiRepository,
        currencyDao: CurrencyDao,
        offlineRatesDao: OfflineRatesDao
    ) = SettingsViewModel(
        preferencesRepository,
        apiRepository,
        currencyDao,
        offlineRatesDao
    )

    @Provides
    @ActivityScope
    internal fun providesCurrenciesViewModel(
        preferencesRepository: PreferencesRepository,
        currencyDao: CurrencyDao
    ) = CurrenciesViewModel(
        preferencesRepository,
        currencyDao
    )

    @Provides
    @FragmentScope
    internal fun providesBarViewModel(currencyDao: CurrencyDao) = BarViewModel(currencyDao)
}
