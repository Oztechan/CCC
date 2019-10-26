package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.main.MainFragment
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.SettingsFragment

@Suppress("unused")
@Module
abstract class FragmentInjectionModule {

    @ContributesAndroidInjector
    abstract fun contributesMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributesSettingsFragment(): SettingsFragment
}
