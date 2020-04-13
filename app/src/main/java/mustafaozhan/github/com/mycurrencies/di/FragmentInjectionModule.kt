package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorFragment
import mustafaozhan.github.com.mycurrencies.ui.main.settings.SettingsFragment

@Suppress("unused")
@Module
abstract class FragmentInjectionModule {

    @ContributesAndroidInjector
    abstract fun contributesCalculatorFragment(): CalculatorFragment

    @ContributesAndroidInjector
    abstract fun contributesSettingsFragment(): SettingsFragment
}
