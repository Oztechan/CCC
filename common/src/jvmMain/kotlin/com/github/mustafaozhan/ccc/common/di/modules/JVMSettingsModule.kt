package com.github.mustafaozhan.ccc.common.di.modules

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope

@ExperimentalSettingsImplementation
actual fun Scope.provideSettings(): Settings = JvmPreferencesSettings(get())
