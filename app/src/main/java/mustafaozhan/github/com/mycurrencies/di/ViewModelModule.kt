package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.data.repository.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.ui.main.activity.MainViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.CalculatorViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.SettingsViewModel
import mustafaozhan.github.com.mycurrencies.ui.slider.SliderViewModel
import mustafaozhan.github.com.mycurrencies.ui.splash.SplashViewModel

@Suppress("unused")
@Module
class ViewModelModule {
    @Provides
    fun provideMainActivityViewModel(preferencesRepository: PreferencesRepository) =
        MainViewModel(preferencesRepository)

    @Provides
    fun provideSliderActivityViewModel(preferencesRepository: PreferencesRepository) =
        SliderViewModel(preferencesRepository)

    @Provides
    fun provideSplashActivityViewModel(preferencesRepository: PreferencesRepository) =
        SplashViewModel(preferencesRepository)

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
