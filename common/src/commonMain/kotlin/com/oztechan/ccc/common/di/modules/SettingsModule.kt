package com.oztechan.ccc.common.di.modules

import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope
import org.koin.dsl.module

val settingsModule = module {
    single { provideSettings() }
}

expect fun Scope.provideSettings(): Settings
