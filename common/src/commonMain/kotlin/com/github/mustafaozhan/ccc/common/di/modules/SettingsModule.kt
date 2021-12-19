package com.github.mustafaozhan.ccc.common.di.modules

import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun getSettingsModule() = module {
    single { provideSettings() }
    single<SettingsRepository> { SettingsRepositoryImp(get()) }
}

expect fun Scope.provideSettings(): Settings
