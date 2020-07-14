/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.di

import com.github.mustafaozhan.ui.main.bar.BarBottomSheetDialogFragment
import com.github.mustafaozhan.ui.main.calculator.CalculatorFragment
import com.github.mustafaozhan.ui.main.currencies.CurrenciesFragment
import com.github.mustafaozhan.ui.main.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentInjectionModule {

    @ContributesAndroidInjector
    abstract fun contributesCalculatorFragment(): CalculatorFragment

    @ContributesAndroidInjector
    abstract fun contributesSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributesCurrenciesFragment(): CurrenciesFragment

    @ContributesAndroidInjector
    abstract fun contributesBarBottomSheetDialogFragment(): BarBottomSheetDialogFragment
}
