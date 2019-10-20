package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivityViewModel
import mustafaozhan.github.com.mycurrencies.main.fragment.MainFragmentViewModel
import mustafaozhan.github.com.mycurrencies.settings.SettingsFragmentViewModel

@Suppress("unused")
@Module
class ViewModelModule {
    @Provides
    fun provideMainActivityViewModel() =
        MainActivityViewModel()

    @Provides
    fun provideMainFragmentViewModel() =
        MainFragmentViewModel()

    @Provides
    fun provideSettingsFragmentViewModel() =
        SettingsFragmentViewModel()
}
