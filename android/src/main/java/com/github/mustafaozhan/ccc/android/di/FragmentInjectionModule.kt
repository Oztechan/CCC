/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.di

import com.github.mustafaozhan.ccc.android.ui.bar.BarBottomSheetDialogFragment
import com.github.mustafaozhan.ccc.android.ui.calculator.CalculatorFragment
import com.github.mustafaozhan.ccc.android.ui.currencies.CurrenciesFragment
import com.github.mustafaozhan.ccc.android.ui.settings.SettingsFragment
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
