package com.oztechan.ccc.client.core.persistence.di

import com.oztechan.ccc.client.core.persistence.FlowPersistence
import com.oztechan.ccc.client.core.persistence.FlowPersistenceImpl
import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.core.persistence.PersistenceImpl
import com.oztechan.ccc.client.core.persistence.SuspendPersistence
import com.oztechan.ccc.client.core.persistence.SuspendPersistenceImpl
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val clientCorePersistenceModule = module {
    single<ObservableSettings> {
        NSUserDefaultsSettings(get<NativeDependencyWrapper>().userDefaults)
    }
    @Suppress("OPT_IN_USAGE")
    single<SuspendSettings> { get<ObservableSettings>().toSuspendSettings() }
    @Suppress("OPT_IN_USAGE")
    single<FlowSettings> { get<ObservableSettings>().toFlowSettings() }

    singleOf(::PersistenceImpl) { bind<Persistence>() }
    @Suppress("OPT_IN_USAGE")
    singleOf(::FlowPersistenceImpl) { bind<FlowPersistence>() }
    @Suppress("OPT_IN_USAGE")
    singleOf(::SuspendPersistenceImpl) { bind<SuspendPersistence>() }
}
