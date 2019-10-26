package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.data.repository.BackendRepository
import mustafaozhan.github.com.mycurrencies.data.repository.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.ui.main.activity.MainActivityViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.main.MainFragmentViewModel
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.SettingsFragmentViewModel
import mustafaozhan.github.com.mycurrencies.ui.slider.SliderActivityViewModel
import mustafaozhan.github.com.mycurrencies.ui.splash.SplashActivityViewModel

@Suppress("unused")
@Module
class ViewModelModule {
    @Provides
    fun provideMainActivityViewModel(preferencesRepository: PreferencesRepository) =
        MainActivityViewModel(preferencesRepository)

    @Provides
    fun provideSliderActivityViewModel(preferencesRepository: PreferencesRepository) =
        SliderActivityViewModel(preferencesRepository)

    @Provides
    fun provideSplashActivityViewModel(preferencesRepository: PreferencesRepository) =
        SplashActivityViewModel(preferencesRepository)

    @Provides
    fun provideMainFragmentViewModel(
        preferencesRepository: PreferencesRepository,
        backendRepository: BackendRepository,
        currencyDao: CurrencyDao,
        offlineRatesDao: OfflineRatesDao
    ) = MainFragmentViewModel(
        preferencesRepository,
        backendRepository,
        currencyDao,
        offlineRatesDao)

    @Provides
    fun provideSettingsFragmentViewModel(
        preferencesRepository: PreferencesRepository,
        currencyDao: CurrencyDao
    ) = SettingsFragmentViewModel(preferencesRepository, currencyDao)
}
