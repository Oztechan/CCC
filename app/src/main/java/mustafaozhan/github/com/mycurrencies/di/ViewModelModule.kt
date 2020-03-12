package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.data.backend.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.data.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.data.room.dao.OfflineRatesDao
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
        currencyDao: CurrencyDao,
        offlineRatesDao: OfflineRatesDao
    ) = CalculatorViewModel(
        preferencesRepository,
        backendRepository,
        currencyDao,
        offlineRatesDao)

    @Provides
    fun provideSettingsFragmentViewModel(
        preferencesRepository: PreferencesRepository,
        currencyDao: CurrencyDao
    ) = SettingsViewModel(preferencesRepository, currencyDao)
}
