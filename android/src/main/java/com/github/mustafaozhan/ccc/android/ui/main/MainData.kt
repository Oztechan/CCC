/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.main

import com.github.mustafaozhan.ccc.android.util.isDayPassed
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository

open class MainData(
    private val settingsRepository: SettingsRepository
) {

    companion object {
        internal const val MINIMUM_ACTIVE_CURRENCY = 2
        internal const val BACK_DELAY: Long = 2000
        internal const val AD_INITIAL_DELAY: Long = 45000
        internal const val REVIEW_DELAY: Long = 10000
        internal const val AD_PERIOD: Long = 180000
        internal const val TEXT_EMAIL_TYPE = "text/email"
        internal const val TEXT_TYPE = "text/plain"
        internal const val KEY_BASE_CURRENCY = "base_currency"
    }

    val isRewardExpired
        get() = adFreeActivatedDate.isDayPassed()

    var firstRun
        get() = settingsRepository.firstRun
        set(value) {
            settingsRepository.firstRun = value
        }

    var currentBase
        get() = settingsRepository.currentBase
        set(value) {
            settingsRepository.currentBase = value
        }

    var appTheme
        get() = settingsRepository.appTheme
        set(value) {
            settingsRepository.appTheme = value
        }

    var adFreeActivatedDate
        get() = settingsRepository.adFreeActivatedDate
        set(value) {
            settingsRepository.adFreeActivatedDate = value
        }

    var lastReviewRequest
        get() = settingsRepository.lastReviewRequest
        set(value) {
            settingsRepository.lastReviewRequest = value
        }
}
