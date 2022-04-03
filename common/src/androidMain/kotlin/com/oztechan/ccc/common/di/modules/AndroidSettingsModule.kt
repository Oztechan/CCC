package com.oztechan.ccc.common.di.modules

import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope

actual fun Scope.provideSettings(): Settings = AndroidSettings(get())
