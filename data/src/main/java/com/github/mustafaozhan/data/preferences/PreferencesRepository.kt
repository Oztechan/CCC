/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.preferences

import android.content.Context
import com.github.mustafaozhan.basemob.data.preferences.BasePreferencesRepository
import com.github.mustafaozhan.data.model.CurrencyType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository
@Inject constructor(
    context: Context
) : BasePreferencesRepository(context) {
    companion object {
        private const val KEY_APPLICATION_PREFERENCES = "application_preferences"
        private const val KEY_FIRST_RUN = "firs_run"
        private const val KEY_CURRENT_BASE = "current_base"
        private const val KEY_AD_FREE_DATE = "ad_free_date"
        private const val KEY_APP_THEME = "app_theme"
    }

    override val preferencesName: String
        get() = KEY_APPLICATION_PREFERENCES

    var firstRun
        get() = getValue(KEY_FIRST_RUN, true)
        set(value) = setValue(KEY_FIRST_RUN, value)

    var currentBase
        get() = getValue(KEY_CURRENT_BASE, CurrencyType.NULL.toString())
        set(value) = setValue(KEY_CURRENT_BASE, value)

    var appTheme
        get() = getValue(KEY_APP_THEME, -1)
        set(value) = setValue(KEY_APP_THEME, value)

    var adFreeActivatedDate
        get() = getValue(KEY_AD_FREE_DATE, 0.toLong())
        set(value) = setValue(KEY_AD_FREE_DATE, value)
}
