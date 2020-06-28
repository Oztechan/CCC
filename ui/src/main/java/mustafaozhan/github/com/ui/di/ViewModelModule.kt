/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.ui.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.data.api.ApiRepository
import mustafaozhan.github.com.data.db.CurrencyDao
import mustafaozhan.github.com.data.db.OfflineRatesDao
import mustafaozhan.github.com.data.preferences.PreferencesRepository
import mustafaozhan.github.com.ui.main.MainViewModel
import mustafaozhan.github.com.ui.main.bar.BarViewModel
import mustafaozhan.github.com.ui.main.calculator.CalculatorViewModel
import mustafaozhan.github.com.ui.main.settings.SettingsViewModel

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
