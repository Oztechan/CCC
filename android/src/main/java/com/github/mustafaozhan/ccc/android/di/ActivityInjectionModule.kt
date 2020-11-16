/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.di

import com.github.mustafaozhan.ccc.android.ui.main.MainActivity
import com.github.mustafaozhan.ccc.android.ui.slider.SliderActivity
import com.github.mustafaozhan.ccc.android.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityInjectionModule {

    @ContributesAndroidInjector
    abstract fun contributesMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributesSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributesSliderActivity(): SliderActivity
}
