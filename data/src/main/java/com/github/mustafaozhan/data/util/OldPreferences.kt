/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.util

import android.content.Context
import com.github.mustafaozhan.scopemob.whether

class OldPreferences(val context: Context) {
    companion object {
        private const val OLD_PREFERENCES_NAME = "GENERAL_SHARED_PREFS"
        private const val OLD_MAIN_DATA_KEY = "MAIN_DATA"
        private const val OLD_BASE_CURRENCY_KEY = "currentBase"
        private const val OLD_FIRST_RUN_KEY = "firstRun"
    }

    fun isOldPreferencesExist() =
        context.getSharedPreferences(OLD_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .all.isNotEmpty()

    fun removeOldPreferences() =
        context.getSharedPreferences(OLD_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(OLD_MAIN_DATA_KEY)
            .apply()

    fun getOldFirstRun() = getOldPreferences()?.get(OLD_FIRST_RUN_KEY)

    fun getOldBaseCurrency() = getOldPreferences()?.get(OLD_BASE_CURRENCY_KEY)

    private fun getOldPreferences(): MutableMap<String, String>? {
        val mainDataMap = mutableMapOf<String, String>()
        context.getSharedPreferences(
            OLD_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        ).getString(OLD_MAIN_DATA_KEY, "")
            ?.dropLast(1)?.drop(1)
            ?.replace("\"", "")
            ?.split(",")
            ?.take(2)
            ?.forEach { value ->
                value.split(":")
                    .whether { size > 1 }
                    ?.let {
                        mainDataMap[it[0]] = it[1]
                    }
            }
        return mainDataMap
    }
}
