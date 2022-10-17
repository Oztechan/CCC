package com.oztechan.ccc.common.di.module.submodule

import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val settingsModule = module {
    @Suppress("OPT_IN_USAGE")
    singleOf(::JvmPreferencesSettings) { bind<Settings>() }
}
