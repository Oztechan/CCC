/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui.main

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.ccc.android.ui.main.MainData.Companion.WEEK
import com.github.mustafaozhan.data.preferences.PreferencesRepository

class MainViewModel(preferencesRepository: PreferencesRepository) : ViewModel() {
    val data = MainData(preferencesRepository)

    fun shouldShowReview() = System.currentTimeMillis() - data.lastReviewRequest >= WEEK

    fun setLastReview() {
        data.lastReviewRequest = System.currentTimeMillis()
    }
}
