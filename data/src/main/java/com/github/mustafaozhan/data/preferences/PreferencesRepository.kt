/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.preferences

import android.content.Context
import com.github.mustafaozhan.data.error.SharedPreferencesException
import com.github.mustafaozhan.data.model.CurrencyType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository
@Inject constructor(context: Context) {
    companion object {
        private const val KEY_APPLICATION_PREFERENCES = "application_preferences"
        private const val KEY_FIRST_RUN = "firs_run"
        private const val KEY_CURRENT_BASE = "current_base"
        private const val KEY_AD_FREE_DATE = "ad_free_date"
        private const val KEY_APP_THEME = "app_theme"
        private const val KEY_LAST_REVIEW_REQUEST = "last_review_request"
    }

    private val preferences = context.getSharedPreferences(
        KEY_APPLICATION_PREFERENCES,
        Context.MODE_PRIVATE
    )

    var firstRun
        get() = preferences.getBoolean(KEY_FIRST_RUN, true)
        set(value) = setValue(KEY_FIRST_RUN, value)

    var currentBase
        get() = preferences.getString(KEY_CURRENT_BASE, CurrencyType.NULL.toString())
            ?: CurrencyType.NULL.toString()
        set(value) = setValue(KEY_CURRENT_BASE, value)

    var appTheme
        get() = preferences.getInt(KEY_APP_THEME, -1)
        set(value) = setValue(KEY_APP_THEME, value)

    var adFreeActivatedDate
        get() = preferences.getLong(KEY_AD_FREE_DATE, 0.toLong())
        set(value) = setValue(KEY_AD_FREE_DATE, value)

    var lastReviewRequest: Long
        get() = preferences.getLong(KEY_LAST_REVIEW_REQUEST, System.currentTimeMillis())
        set(value) = setValue(KEY_LAST_REVIEW_REQUEST, value)

    @Suppress("UNCHECKED_CAST")
    fun <T> setValue(key: String, value: T) = with(preferences.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Set<*> -> putStringSet(key, value as Set<String>)
            is MutableSet<*> -> putStringSet(key, value as MutableSet<String>)
            else -> throw SharedPreferencesException()
        }.apply()
    }
}
