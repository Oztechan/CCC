/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseViewModel
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.util.isWeekPassed
import com.github.mustafaozhan.ccc.common.data.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.log.kermit
import kotlinx.datetime.Clock

class MainViewModel(private val settingsRepository: SettingsRepository) : BaseViewModel() {

    init {
        kermit.d { "MainViewModel init" }
    }

    fun shouldShowReview() = settingsRepository.lastReviewRequest.isWeekPassed()

    fun setLastReview() {
        settingsRepository.lastReviewRequest = Clock.System.now().toEpochMilliseconds()
    }

    fun isFistRun() = settingsRepository.firstRun

    fun getAppTheme() = settingsRepository.appTheme

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()

    override fun onCleared() {
        kermit.d { "MainViewModel onCleared" }
        super.onCleared()
    }
}
