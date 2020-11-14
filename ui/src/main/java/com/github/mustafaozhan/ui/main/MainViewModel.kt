/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main

import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.MainData.Companion.WEEK
import javax.inject.Inject

class MainViewModel
@Inject constructor(preferencesRepository: PreferencesRepository) : BaseViewModel() {
    val data = MainData(preferencesRepository)

    fun shouldShowReview() = System.currentTimeMillis() - data.lastReviewRequest >= WEEK

    fun setLastReview() {
        data.lastReviewRequest = System.currentTimeMillis()
    }
}
