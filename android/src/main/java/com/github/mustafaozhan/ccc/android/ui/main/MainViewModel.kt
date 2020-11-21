/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.main

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.android.ui.main.MainData.Companion.WEEK
import com.github.mustafaozhan.ccc.client.repo.SettingsRepository

class MainViewModel(settingsRepository: SettingsRepository) : ViewModel() {
    val data = MainData(settingsRepository)

    fun shouldShowReview() = System.currentTimeMillis() - data.lastReviewRequest >= WEEK

    fun setLastReview() {
        data.lastReviewRequest = System.currentTimeMillis()
    }
}
