package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.russhwolf.settings.coroutines.SuspendSettings

@Suppress("OPT_IN_USAGE")
class SuspendPersistenceImpl(private val suspendSettings: SuspendSettings) : SuspendPersistence {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> getSuspend(key: String, defaultValue: T): T =
        when (defaultValue) {
            is Long -> suspendSettings.getLong(key, defaultValue) as T
            is String -> suspendSettings.getString(key, defaultValue) as T
            is Int -> suspendSettings.getInt(key, defaultValue) as T
            is Boolean -> suspendSettings.getBoolean(key, defaultValue) as T
            is Float -> suspendSettings.getFloat(key, defaultValue) as T
            else -> throw UnsupportedPersistenceException()
        }

    override suspend fun <T : Any> setSuspend(key: String, value: T) = when (value) {
        is Long -> suspendSettings.putLong(key, value)
        is String -> suspendSettings.putString(key, value)
        is Int -> suspendSettings.putInt(key, value)
        is Boolean -> suspendSettings.putBoolean(key, value)
        is Float -> suspendSettings.putFloat(key, value)
        else -> throw UnsupportedPersistenceException()
    }
}
