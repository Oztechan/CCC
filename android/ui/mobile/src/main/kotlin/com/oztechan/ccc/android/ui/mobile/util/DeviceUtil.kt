/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.util

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.oztechan.ccc.client.core.shared.model.AppTheme
import com.scottyab.rootbeer.RootBeer
import java.util.Locale

fun updateBaseContextLocale(context: Context): Context? {
    val locale = Locale.US
    Locale.setDefault(locale)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        updateResourcesLocale(context, locale)
    } else {
        updateResourcesLocaleLegacy(context, locale)
    }
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

@TargetApi(Build.VERSION_CODES.N)
private fun updateResourcesLocale(context: Context, locale: Locale): Context? {
    val configuration: Configuration = context.resources.configuration
    configuration.setLocale(locale)
    return context.createConfigurationContext(configuration)
}

@Suppress("DEPRECATION")
private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
    val resources: Resources = context.resources
    val configuration: Configuration = resources.configuration
    configuration.locale = locale
    resources.updateConfiguration(configuration, resources.displayMetrics)
    return context
}
