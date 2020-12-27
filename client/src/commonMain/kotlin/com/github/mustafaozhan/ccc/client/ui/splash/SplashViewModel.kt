/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.ui.splash

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.common.log.kermit
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository

class SplashViewModel(private val settingsRepository: SettingsRepository) : BaseViewModel() {

    init {
        kermit.d { "SplashViewModel init" }
    }

    fun getAppTheme() = settingsRepository.appTheme

    fun isFirstRun() = settingsRepository.firstRun

    override fun onCleared() {
        kermit.d { "SplashViewModel onCleared" }
        super.onCleared()
    }
}
