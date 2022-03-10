package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.di.NativeDependencyWrapper
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope

actual fun Scope.provideSettings(): Settings = AppleSettings(
    get<NativeDependencyWrapper>().userDefaults
)
