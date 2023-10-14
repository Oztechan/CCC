package com.oztechan.ccc.client.core.persistence.di

import android.content.Context
import android.content.SharedPreferences
import com.oztechan.ccc.client.core.persistence.FlowPersistence
import com.oztechan.ccc.client.core.persistence.FlowPersistenceImpl
import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.core.persistence.PersistenceImpl
import com.oztechan.ccc.client.core.persistence.SuspendPersistence
import com.oztechan.ccc.client.core.persistence.SuspendPersistenceImpl
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

actual val clientCorePersistenceModule = module {
    singleOf(::provideSharedPreferences)

    single<ObservableSettings> { SharedPreferencesSettings(get()) }
    single<Settings> { get<ObservableSettings>() }
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

private fun provideSharedPreferences(
    context: Context
): SharedPreferences = context.getSharedPreferences(
    KEY_APPLICATION_PREFERENCES,
    Context.MODE_PRIVATE
)
