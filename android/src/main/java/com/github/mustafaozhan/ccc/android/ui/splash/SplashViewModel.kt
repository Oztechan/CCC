/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.splash

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.data.preferences.PreferencesRepository

class SplashViewModel(private val preferencesRepository: PreferencesRepository) : ViewModel() {

    fun getAppTheme() = preferencesRepository.appTheme

    fun isFirstRun() = preferencesRepository.firstRun
}
