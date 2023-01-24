package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.russhwolf.settings.Settings

internal class PersistenceImpl(private val settings: Settings) : Persistence {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getValue(key: String, defaultValue: T): T = when (defaultValue) {
        is Long -> settings.getLong(key, defaultValue) as T
        is String -> settings.getString(key, defaultValue) as T
        is Int -> settings.getInt(key, defaultValue) as T
        is Boolean -> settings.getBoolean(key, defaultValue) as T
        is Float -> settings.getFloat(key, defaultValue) as T
        else -> defaultValue
    }

    override fun <T : Any> setValue(key: String, value: T) = when (value) {
        is Long -> settings.putLong(key, value)
        is String -> settings.putString(key, value)
        is Int -> settings.putInt(key, value)
        is Boolean -> settings.putBoolean(key, value)
        is Float -> settings.putFloat(key, value)
        else -> throw UnsupportedPersistenceException()
    }
}
