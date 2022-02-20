/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.model

@Suppress("MagicNumber")
enum class AppTheme(val order: Int, val typeName: String, val themeValue: Int) {
    LIGHT(0, "Light", 1),
    DARK(1, "Dark", 2),
    SYSTEM_DEFAULT(2, "System default", -1);

    companion object {
        // dark theme applies below SDK 29
        const val SYSTEM_DARK = "System dark"

        fun getThemeByValue(value: Int) = values().firstOrNull {
            it.themeValue == value
        }

        fun getThemeByValueOrDefault(value: Int) = getThemeByValue(value) ?: SYSTEM_DEFAULT

        fun getThemeByOrder(order: Int) = values().firstOrNull {
            it.order == order
        }

        fun getThemeByOrderOrDefault(order: Int) = getThemeByOrder(order) ?: SYSTEM_DEFAULT
    }
}
