package com.oztechan.ccc.common.di.modules.submodules

import com.oztechan.ccc.common.di.NativeDependencyWrapper
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

internal actual val settingsModule = module {
    single<Settings> {
        AppleSettings(get<NativeDependencyWrapper>().userDefaults)
    }
}
