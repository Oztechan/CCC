/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.ui.splash

import com.github.mustafaozhan.ccc.client.base.BaseUseCase
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository

class SplashUseCase(private val settingsRepository: SettingsRepository) : BaseUseCase() {

    fun getAppTheme() = settingsRepository.appTheme

    fun isFirstRun() = settingsRepository.firstRun
}
