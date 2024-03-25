package com.oztechan.ccc.client.core.persistence.di

import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.core.persistence.PersistenceImpl
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val clientCorePersistenceModule = module {
    single<Settings> {
        NSUserDefaultsSettings(get<NativeDependencyWrapper>().userDefaults)
    }
    singleOf(::PersistenceImpl) { bind<Persistence>() }
}
