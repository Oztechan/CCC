package com.oztechan.ccc.common.di.modules

import com.oztechan.ccc.common.settings.SettingsRepository
import com.oztechan.ccc.common.settings.SettingsRepositoryImp
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun getSettingsModule() = module {
    single { provideSettings() }
    singleOf(::SettingsRepositoryImp) { bind<SettingsRepository>() }
}

expect fun Scope.provideSettings(): Settings
