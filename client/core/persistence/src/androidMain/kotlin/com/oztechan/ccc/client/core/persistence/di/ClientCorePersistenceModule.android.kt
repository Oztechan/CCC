package com.oztechan.ccc.client.core.persistence.di

import android.content.Context
import android.content.SharedPreferences
import com.oztechan.ccc.client.core.persistence.FlowPersistence
import com.oztechan.ccc.client.core.persistence.FlowPersistenceImpl
import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.core.persistence.PersistenceImpl
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val KEY_APPLICATION_PREFERENCES = "application_preferences"

actual val clientCorePersistenceModule = module {
    singleOf(::provideSharedPreferences)
    single<ObservableSettings> { SharedPreferencesSettings(get()) }
    single<Settings> { get<ObservableSettings>() }
    singleOf(::PersistenceImpl) { bind<Persistence>() }
    singleOf(::FlowPersistenceImpl) { bind<FlowPersistence>() }
}

private fun provideSharedPreferences(
    context: Context
): SharedPreferences = context.getSharedPreferences(
    KEY_APPLICATION_PREFERENCES,
    Context.MODE_PRIVATE
)
