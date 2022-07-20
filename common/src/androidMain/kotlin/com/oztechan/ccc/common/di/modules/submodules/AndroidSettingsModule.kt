package com.oztechan.ccc.common.di.modules.submodules

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

internal actual val settingsModule = module {
    singleOf(::provideSharedPreferences)
    singleOf(::AndroidSettings) { bind<Settings>() }
}

private fun provideSharedPreferences(
    context: Context
): SharedPreferences = context.getSharedPreferences(
    KEY_APPLICATION_PREFERENCES,
    Context.MODE_PRIVATE
)
