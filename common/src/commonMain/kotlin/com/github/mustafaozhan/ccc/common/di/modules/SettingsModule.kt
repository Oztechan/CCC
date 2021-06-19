package com.github.mustafaozhan.ccc.common.di.modules

import com.github.mustafaozhan.ccc.common.di.getSettingsDefinition
import com.github.mustafaozhan.ccc.common.fake.FakeSettings
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.settings.SettingsRepositoryImp
import org.koin.dsl.module

fun getSettingsModule(forTest: Boolean = false) = module {
    if (forTest) {
        single { FakeSettings.getSettings() }
    } else {
        getSettingsDefinition()
    }

    single<SettingsRepository> { SettingsRepositoryImp(get()) }
}
