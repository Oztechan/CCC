/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.common.data.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.log.kermit

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
