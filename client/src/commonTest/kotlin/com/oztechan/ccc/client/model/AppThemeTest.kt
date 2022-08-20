package com.oztechan.ccc.client.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AppThemeTest {
    @Test
    fun getThemeByValue() {
        val appTheme = AppTheme.getThemeByValue(3)
        assertEquals(null, appTheme)

        AppTheme.values().forEach {
            assertEquals(it, AppTheme.getThemeByValue(it.themeValue))
        }
    }

    @Test
    fun getThemeByValueOrDefault() {
        val appTheme = AppTheme.getThemeByValueOrDefault(3)
        assertEquals(AppTheme.SYSTEM_DEFAULT, appTheme)

        AppTheme.values().forEach {
            assertEquals(it, AppTheme.getThemeByValueOrDefault(it.themeValue))
        }
    }

    @Test
    fun getThemeByOrder() {
        val appTheme = AppTheme.getThemeByOrder(3)
        assertEquals(null, appTheme)

        AppTheme.values().forEach {
            assertEquals(it, AppTheme.getThemeByOrder(it.order))
        }
    }

    @Test
    fun getThemeByOrderOrDefault() {
        val appTheme = AppTheme.getThemeByOrderOrDefault(3)
        assertEquals(AppTheme.SYSTEM_DEFAULT, appTheme)

        AppTheme.values().forEach {
            assertEquals(it, AppTheme.getThemeByOrderOrDefault(it.order))
        }
    }

    @Test
    fun getAnalyticsThemeName() = with(AppTheme) {
        assertEquals(AppTheme.SYSTEM_DEFAULT.themeName, getAnalyticsThemeName(1, Device.IOS))

        assertEquals(SYSTEM_DARK, getAnalyticsThemeName(1, Device.Android.Google(28)))
        assertEquals(SYSTEM_DARK, getAnalyticsThemeName(1, Device.Android.Huawei(28)))

        assertEquals(getThemeByValueOrDefault(1).themeName, getAnalyticsThemeName(1, Device.Android.Google(30)))
        assertEquals(getThemeByValueOrDefault(1).themeName, getAnalyticsThemeName(1, Device.Android.Huawei(30)))
    }
}
