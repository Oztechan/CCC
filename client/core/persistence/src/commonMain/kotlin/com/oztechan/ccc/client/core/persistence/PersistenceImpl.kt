package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getFloatFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getStringFlow
import kotlinx.coroutines.flow.Flow

internal class PersistenceImpl(private val settings: ObservableSettings) : Persistence {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getValue(key: String, defaultValue: T): T = when (defaultValue) {
        is Long -> settings.getLong(key, defaultValue) as T
        is String -> settings.getString(key, defaultValue) as T
        is Int -> settings.getInt(key, defaultValue) as T
        is Boolean -> settings.getBoolean(key, defaultValue) as T
        is Float -> settings.getFloat(key, defaultValue) as T
        else -> throw UnsupportedPersistenceException()
    }

    override fun <T : Any> setValue(key: String, value: T) = when (value) {
        is Long -> settings.putLong(key, value)
        is String -> settings.putString(key, value)
        is Int -> settings.putInt(key, value)
        is Boolean -> settings.putBoolean(key, value)
        is Float -> settings.putFloat(key, value)
        else -> throw UnsupportedPersistenceException()
    }

    @Suppress("UNCHECKED_CAST", "OPT_IN_USAGE")
    override fun <T : Any> getFlow(key: String, defaultValue: T): Flow<T> =
        when (defaultValue) {
            is Long -> settings.getLongFlow(key, defaultValue) as Flow<T>
            is String -> settings.getStringFlow(key, defaultValue) as Flow<T>
            is Int -> settings.getIntFlow(key, defaultValue) as Flow<T>
            is Boolean -> settings.getBooleanFlow(key, defaultValue) as Flow<T>
            is Float -> settings.getFloatFlow(key, defaultValue) as Flow<T>
            else -> {
                settings
                throw UnsupportedPersistenceException()
            }
        }
}
