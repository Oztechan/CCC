/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.main.model

import androidx.appcompat.app.AppCompatDelegate

@Suppress("MagicNumber")
enum class AppTheme(val order: Int, val typeName: String, val themeValue: Int) {
    LIGHT(0, "Light", AppCompatDelegate.MODE_NIGHT_NO),
    DARK(1, "Dark", AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM_DEFAULT(2, "System default", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        fun getThemeByValue(value: Int) = values().firstOrNull { it.themeValue == value }
        fun getThemeByOrder(order: Int) = values().firstOrNull { it.order == order }
    }
}
