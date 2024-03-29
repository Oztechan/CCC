package com.oztechan.ccc.android.ui.mobile.util

import androidx.appcompat.app.AppCompatDelegate
import com.oztechan.ccc.client.core.shared.model.AppTheme
import org.junit.Test
import kotlin.test.assertEquals

internal class DeviceUtilTest {
    @Test
    fun getThemeMode() {
        assertEquals(
            AppCompatDelegate.MODE_NIGHT_NO,
            getThemeMode(AppTheme.LIGHT.themeValue)
        )
        assertEquals(
            AppCompatDelegate.MODE_NIGHT_YES,
            getThemeMode(AppTheme.DARK.themeValue)
        )
        assertEquals(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            getThemeMode(AppTheme.SYSTEM_DEFAULT.themeValue)
        )
        assertEquals(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            getThemeMode(5)
        )
    }
}
