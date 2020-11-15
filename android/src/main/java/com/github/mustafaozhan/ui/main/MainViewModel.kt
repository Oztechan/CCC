/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main

import androidx.lifecycle.ViewModel
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.MainData.Companion.WEEK
import javax.inject.Inject

class MainViewModel
@Inject constructor(preferencesRepository: PreferencesRepository) : ViewModel() {
    val data = MainData(preferencesRepository)

    fun shouldShowReview() = System.currentTimeMillis() - data.lastReviewRequest >= WEEK

    fun setLastReview() {
        data.lastReviewRequest = System.currentTimeMillis()
    }
}
