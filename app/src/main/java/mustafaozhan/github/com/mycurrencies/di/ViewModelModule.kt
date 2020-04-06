package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.data.backend.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.currency.CurrencyRepository
import mustafaozhan.github.com.mycurrencies.data.room.offlineRates.OfflineRatesRepository
import mustafaozhan.github.com.mycurrencies.ui.main.activity.MainViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.CalculatorViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.SettingsViewModel

@Suppress("unused")
@Module
class ViewModelModule {
    @Provides
    fun provideMainActivityViewModel(preferencesRepository: PreferencesRepository) =
        MainViewModel(preferencesRepository)

    @Provides
    fun provideMainFragmentViewModel(
        preferencesRepository: PreferencesRepository,
        backendRepository: BackendRepository,
        currencyRepository: CurrencyRepository,
        offlineRatesRepository: OfflineRatesRepository
    ) = CalculatorViewModel(
        preferencesRepository,
        backendRepository,
        currencyRepository,
        offlineRatesRepository)

    @Provides
    fun provideSettingsFragmentViewModel(
        preferencesRepository: PreferencesRepository,
        currencyRepository: CurrencyRepository
    ) = SettingsViewModel(preferencesRepository, currencyRepository)
}
