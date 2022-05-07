package com.oztechan.ccc.common.di.modules

import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope

@Suppress("OPT_IN_USAGE")
actual fun Scope.provideSettings(): Settings = JvmPreferencesSettings(get())
