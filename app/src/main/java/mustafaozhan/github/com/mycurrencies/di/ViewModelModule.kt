/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.data.api.ApiRepository
import mustafaozhan.github.com.mycurrencies.data.db.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.db.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.MainViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar.BarViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.settings.SettingsViewModel

@Suppress("unused")
@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    internal fun providesMainViewModel(preferencesRepository: PreferencesRepository) =
        MainViewModel(preferencesRepository)

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
        offlineRatesDao)

    @Provides
    @ActivityScope
    internal fun providesSettingsViewModel(
        preferencesRepository: PreferencesRepository,
        currencyDao: CurrencyDao
    ) = SettingsViewModel(preferencesRepository, currencyDao)

    @Provides
    @FragmentScope
    internal fun providesBarViewModel(currencyDao: CurrencyDao) = BarViewModel(currencyDao)
}
