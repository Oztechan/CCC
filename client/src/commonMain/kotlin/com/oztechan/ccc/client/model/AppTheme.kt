/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.model

import com.oztechan.ccc.client.core.infrastructure.Device

@Suppress("MagicNumber")
enum class AppTheme(val themeName: String, val themeValue: Int) {
    LIGHT("Light", 1),
    DARK("Dark", 2),
    SYSTEM_DEFAULT("System default", -1);

    companion object {
        internal const val SYSTEM_DARK = "System dark"

        fun getThemeByValue(value: Int) = values().firstOrNull { it.themeValue == value }

        fun getThemeByValueOrDefault(value: Int) = getThemeByValue(value) ?: SYSTEM_DEFAULT

        fun getThemeByOrdinal(ordinal: Int) = values().firstOrNull { it.ordinal == ordinal }

        fun getThemeByOrdinalOrDefault(ordinal: Int) = getThemeByOrdinal(ordinal) ?: SYSTEM_DEFAULT

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
