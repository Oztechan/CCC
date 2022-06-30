package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.settings.SettingsRepository
import com.oztechan.ccc.common.settings.SettingsRepositoryImp
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope
import org.koin.dsl.module

val settingsModule = module {
    single { provideSettings() }
    single<SettingsRepository> { SettingsRepositoryImp(get()) }
}

expect fun Scope.provideSettings(): Settings
