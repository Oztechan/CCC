package com.github.mustafaozhan.ccc.common.di.modules

import com.github.mustafaozhan.ccc.common.di.NativeDependencyWrapper
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope

actual fun Scope.provideSettings(): Settings = AppleSettings(
    get<NativeDependencyWrapper>().userDefaults
)
