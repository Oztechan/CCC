package com.oztechan.ccc.client.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppThemeTest {
    @Test
    fun getThemeByValue() {
        val appTheme = AppTheme.getThemeByValue(3)
        assertNull(appTheme)

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
    fun getThemeByOrdinal() {
        val appTheme = AppTheme.getThemeByOrdinal(3)
        assertNull(appTheme)

        AppTheme.values().forEach {
            assertEquals(it, AppTheme.getThemeByOrdinal(it.ordinal))
        }
    }

    @Test
    fun getThemeByOrdinalOrDefault() {
        val appTheme = AppTheme.getThemeByOrdinalOrDefault(3)
        assertEquals(AppTheme.SYSTEM_DEFAULT, appTheme)

        AppTheme.values().forEach {
            assertEquals(it, AppTheme.getThemeByOrdinalOrDefault(it.ordinal))
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
