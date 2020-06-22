/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mustafaozhan.github.com.mycurrencies.ui.main.bar.BarBottomSheetDialogFragment
import mustafaozhan.github.com.mycurrencies.ui.main.calculator.CalculatorFragment
import mustafaozhan.github.com.mycurrencies.ui.main.settings.SettingsFragment

@Suppress("unused")
@Module
abstract class FragmentInjectionModule {

    @ContributesAndroidInjector
    abstract fun contributesCalculatorFragment(): CalculatorFragment

    @ContributesAndroidInjector
    abstract fun contributesSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributesBarBottomSheetDialogFragment(): BarBottomSheetDialogFragment
}
