package com.github.mustafaozhan.ccc.common.di.modules

import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope

@Suppress("EXPERIMENTAL_API_USAGE")
actual fun Scope.provideSettings(): Settings = JvmPreferencesSettings(get())
