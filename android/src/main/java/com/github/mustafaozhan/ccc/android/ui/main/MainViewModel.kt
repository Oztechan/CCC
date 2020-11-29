/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.main

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.android.util.isRewardExpired
import com.github.mustafaozhan.ccc.android.util.isWeekPassed
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import kotlinx.datetime.Clock

class MainViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    fun shouldShowReview() = settingsRepository.lastReviewRequest.isWeekPassed()

    fun setLastReview() {
        settingsRepository.lastReviewRequest = Clock.System.now().toEpochMilliseconds()
    }

    fun isFistRun() = settingsRepository.firstRun

    fun getAppTheme() = settingsRepository.appTheme

    fun isRewardExpired() = settingsRepository.adFreeActivatedDate.isRewardExpired()
}
