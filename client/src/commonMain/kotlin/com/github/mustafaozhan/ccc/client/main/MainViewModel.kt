/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.main

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.common.platform.PlatformRepository

class MainViewModel(
    private val platformRepository: PlatformRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel() {
    init {
        kermit.d { "MainViewModel" }
    }

    var runCounter: Int
        get() {
            settingsRepository.runCounter++
            return settingsRepository.runCounter
        }
        set(value) {
            settingsRepository.runCounter = value
        }

    fun getPlatformName() = platformRepository.platform.toString()

    fun getAppName() = platformRepository.appName
}
