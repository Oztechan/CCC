/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.splash

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository

class SplashViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    fun getAppTheme() = settingsRepository.appTheme

    fun isFirstRun() = settingsRepository.firstRun
}
