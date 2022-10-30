/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.storage.app

import com.russhwolf.settings.Settings

internal class AppStorageImpl(
    private val settings: Settings
) : AppStorage {

    override var firstRun
        get() = settings.getBoolean(KEY_FIRST_RUN, DEFAULT_FIRST_RUN)
        set(value) = settings.putBoolean(KEY_FIRST_RUN, value)

    override var appTheme
        get() = settings.getInt(KEY_APP_THEME, DEFAULT_APP_THEME)
        set(value) = settings.putInt(KEY_APP_THEME, value)

    override var adFreeEndDate
        get() = settings.getLong(KEY_AD_FREE_END_DATE, DEFAULT_AD_FREE_END_DATE)
        set(value) = settings.putLong(KEY_AD_FREE_END_DATE, value)

    override var sessionCount: Long
        get() = settings.getLong(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT)
        set(value) = settings.putLong(KEY_SESSION_COUNT, value)

    companion object {
        internal const val KEY_FIRST_RUN = "firs_run"
        internal const val KEY_AD_FREE_END_DATE = "ad_free_end_date"
        internal const val KEY_APP_THEME = "app_theme"
        internal const val KEY_SESSION_COUNT = "session_count"

        internal const val DEFAULT_FIRST_RUN = true
        internal const val DEFAULT_AD_FREE_END_DATE = 0L
        internal const val DEFAULT_APP_THEME = -1
        internal const val DEFAULT_SESSION_COUNT = 0L
    }
}
