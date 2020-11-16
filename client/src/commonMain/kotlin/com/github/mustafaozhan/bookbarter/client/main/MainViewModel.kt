/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.client.main

import com.github.mustafaozhan.bookbarter.client.base.BaseViewModel
import com.github.mustafaozhan.bookbarter.client.repo.SettingsRepository
import com.github.mustafaozhan.bookbarter.common.di.kermit
import com.github.mustafaozhan.bookbarter.common.repository.PlatformRepository

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
