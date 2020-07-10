/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.util

import android.content.Context
import com.github.mustafaozhan.scopemob.whether

private const val OLD_PREFERENCES_NAME = "GENERAL_SHARED_PREFS"
private const val OLD_MAIN_DATA_KEY = "MAIN_DATA"

fun getOldPreferences(context: Context): MutableList<String?>? {
    val preferences = context.getSharedPreferences(
        OLD_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    if (preferences.all.isNotEmpty()) {
        val mainDataMap = mutableMapOf<String, String>()

        preferences.getString(OLD_MAIN_DATA_KEY, "")
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
        preferences.edit().remove(OLD_MAIN_DATA_KEY).apply()

        return mainDataMap.values.toMutableList()
    } else {
        return null
    }
}
