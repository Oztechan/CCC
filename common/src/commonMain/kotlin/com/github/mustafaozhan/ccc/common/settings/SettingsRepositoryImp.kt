/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.settings

import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.russhwolf.settings.Settings

internal class SettingsRepositoryImp(
    private val settings: Settings
) : SettingsRepository {

    override var firstRun
        get() = settings.getBoolean(KEY_FIRST_RUN, true)
        set(value) = settings.putBoolean(KEY_FIRST_RUN, value)

    override var currentBase
        get() = settings.getString(KEY_CURRENT_BASE, "")
        set(value) = settings.putString(KEY_CURRENT_BASE, value)

    override var appTheme
        get() = settings.getInt(KEY_APP_THEME, -1)
        set(value) = settings.putInt(KEY_APP_THEME, value)

    override var adFreeEndDate
        get() = settings.getLong(KEY_AD_FREE_END_DATE, 0.toLong())
        set(value) = settings.putLong(KEY_AD_FREE_END_DATE, value)

    override var lastReviewRequest: Long
        get() = settings.getLong(KEY_LAST_REVIEW_REQUEST, nowAsLong())
        set(value) = settings.putLong(KEY_LAST_REVIEW_REQUEST, value)

    companion object {
        private const val KEY_FIRST_RUN = "firs_run"
        private const val KEY_CURRENT_BASE = "current_base"
        private const val KEY_AD_FREE_END_DATE = "ad_free_end_date"
        private const val KEY_APP_THEME = "app_theme"
        private const val KEY_LAST_REVIEW_REQUEST = "last_review_request"
    }
}
