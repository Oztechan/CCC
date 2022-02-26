package com.github.mustafaozhan.ccc.client.model

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
}
