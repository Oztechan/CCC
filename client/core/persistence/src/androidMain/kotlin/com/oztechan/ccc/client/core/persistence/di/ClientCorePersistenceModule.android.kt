package com.oztechan.ccc.client.core.persistence.di

import android.content.Context
import android.content.SharedPreferences
import com.oztechan.ccc.client.core.persistence.FlowPersistence
import com.oztechan.ccc.client.core.persistence.FlowPersistenceImpl
import com.oztechan.ccc.client.core.persistence.SuspendPersistence
import com.oztechan.ccc.client.core.persistence.SuspendPersistenceImpl
import com.oztechan.ccc.common.core.infrastructure.di.DISPATCHER_IO
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

actual val clientCorePersistenceModule = module {
    singleOf(::provideSharedPreferences)

    single<ObservableSettings> { SharedPreferencesSettings(get()) }

    single<FlowPersistence> {
        FlowPersistenceImpl(get<ObservableSettings>().toFlowSettings(get(named(DISPATCHER_IO))))
    }
    single<SuspendPersistence> {
        SuspendPersistenceImpl(get<ObservableSettings>().toSuspendSettings(get(named(DISPATCHER_IO))))
    }
}

private fun provideSharedPreferences(
    context: Context
): SharedPreferences = context.getSharedPreferences(
    KEY_APPLICATION_PREFERENCES,
    Context.MODE_PRIVATE
)
