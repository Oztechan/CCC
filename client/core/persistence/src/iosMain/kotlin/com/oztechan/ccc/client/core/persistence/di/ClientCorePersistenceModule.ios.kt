package com.oztechan.ccc.client.core.persistence.di

import com.oztechan.ccc.client.core.persistence.FlowPersistence
import com.oztechan.ccc.client.core.persistence.FlowPersistenceImpl
import com.oztechan.ccc.client.core.persistence.SuspendPersistence
import com.oztechan.ccc.client.core.persistence.SuspendPersistenceImpl
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val clientCorePersistenceModule = module {
    single<ObservableSettings> {
        NSUserDefaultsSettings(get<NativeDependencyWrapper>().userDefaults)
    }

    @Suppress("OPT_IN_USAGE")
    single<FlowPersistence> {
        FlowPersistenceImpl(get<ObservableSettings>().toFlowSettings(get(named(DISPATCHER_IO))))
    }
    @Suppress("OPT_IN_USAGE")
    single<SuspendPersistence> {
        SuspendPersistenceImpl(get<ObservableSettings>().toSuspendSettings(get(named(DISPATCHER_IO))))
    }
}
