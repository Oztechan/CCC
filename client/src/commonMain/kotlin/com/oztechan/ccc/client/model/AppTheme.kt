/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.model

@Suppress("MagicNumber")
enum class AppTheme(val order: Int, val themeName: String, val themeValue: Int) {
    LIGHT(0, "Light", 1),
    DARK(1, "Dark", 2),
    SYSTEM_DEFAULT(2, "System default", -1);

    companion object {
        internal const val SYSTEM_DARK = "System dark"

        fun getThemeByValue(value: Int) = values().firstOrNull { it.themeValue == value }

        fun getThemeByValueOrDefault(value: Int) = getThemeByValue(value) ?: SYSTEM_DEFAULT

        fun getThemeByOrder(order: Int) = values().firstOrNull { it.order == order }

        fun getThemeByOrderOrDefault(order: Int) = getThemeByOrder(order) ?: SYSTEM_DEFAULT

        @Suppress("MagicNumber")
        fun getAnalyticsThemeName(themeValue: Int, device: Device) = when (device) {
            Device.IOS -> SYSTEM_DEFAULT.themeName
            is Device.Android -> if (device.versionCode < 29) {
                SYSTEM_DARK
            } else {
                getThemeByValueOrDefault(themeValue).themeName
            }
        }
    }
}
