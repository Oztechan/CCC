/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.ui.splash

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository

class SplashViewModel(private val settingsRepository: SettingsRepository) : BaseViewModel() {

    fun getAppTheme() = settingsRepository.appTheme

    fun isFirstRun() = settingsRepository.firstRun
}
