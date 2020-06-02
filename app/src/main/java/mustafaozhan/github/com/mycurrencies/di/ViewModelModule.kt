/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.data.api.ApiRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.MainViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar.BarViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.settings.SettingsViewModel

@Suppress("unused")
@Module
class ViewModelModule {
    @Provides
    fun provideMainViewModel(preferencesRepository: PreferencesRepository) =
        MainViewModel(preferencesRepository)

    @Provides
    fun provideCalculatorViewModel(
        preferencesRepository: PreferencesRepository,
        apiRepository: ApiRepository,
        currencyRepository: CurrencyRepository,
        offlineRatesRepository: OfflineRatesRepository
    ) = CalculatorViewModel(
        preferencesRepository,
        apiRepository,
        currencyRepository,
        offlineRatesRepository)

    @Provides
    fun provideSettingsViewModel(
        preferencesRepository: PreferencesRepository,
        currencyRepository: CurrencyRepository
    ) = SettingsViewModel(preferencesRepository, currencyRepository)

    @Provides
    fun providesBarViewModel(
        currencyRepository: CurrencyRepository
    ) = BarViewModel(currencyRepository)
}
