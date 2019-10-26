package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivityViewModel
import mustafaozhan.github.com.mycurrencies.main.fragment.main.MainFragmentViewModel
import mustafaozhan.github.com.mycurrencies.main.fragment.settings.SettingsFragmentViewModel
import mustafaozhan.github.com.mycurrencies.repository.old.DataManager
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.dao.OfflineRatesDao
import mustafaozhan.github.com.mycurrencies.slider.SliderActivityViewModel
import mustafaozhan.github.com.mycurrencies.splash.SplashActivityViewModel

@Suppress("unused")
@Module
class ViewModelModule {
    @Provides
    fun provideMainActivityViewModel(dataManager: DataManager) =
        MainActivityViewModel(dataManager)

    @Provides
    fun provideSliderActivityViewModel(dataManager: DataManager) =
        SliderActivityViewModel(dataManager)

    @Provides
    fun provideSplashActivityViewModel(dataManager: DataManager) =
        SplashActivityViewModel(dataManager)

    @Provides
    fun provideMainFragmentViewModel(
        dataManager: DataManager,
        currencyDao: CurrencyDao,
        offlineRatesDao: OfflineRatesDao
    ) = MainFragmentViewModel(
        dataManager,
        currencyDao,
        offlineRatesDao)

    @Provides
    fun provideSettingsFragmentViewModel(
        dataManager: DataManager,
        currencyDao: CurrencyDao
    ) = SettingsFragmentViewModel(dataManager, currencyDao)
}
