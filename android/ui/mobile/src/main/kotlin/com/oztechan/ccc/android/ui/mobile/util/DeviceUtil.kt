/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.util

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.oztechan.ccc.client.core.shared.model.AppTheme
import com.scottyab.rootbeer.RootBeer
import java.util.Locale

fun updateBaseContextLocale(context: Context): Context? {
    val locale = Locale.US
    Locale.setDefault(locale)
    val configuration: Configuration = context.resources.configuration
    configuration.setLocale(locale)
    return context.createConfigurationContext(configuration)
}

fun isDeviceRooted(context: Context) = if (RootBeer(context).isRooted) {
    true.toString()
} else {
    false.toString()
}

fun getThemeMode(appThemeValue: Int) = when (AppTheme.getThemeByValue(appThemeValue)) {
    AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
    AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
}
