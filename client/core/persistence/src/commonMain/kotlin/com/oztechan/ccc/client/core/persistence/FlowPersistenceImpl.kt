package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow

@Suppress("OPT_IN_USAGE")
class FlowPersistenceImpl(private val flowSettings: FlowSettings) : FlowPersistence {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getFlow(key: String, defaultValue: T): Flow<T> =
        when (defaultValue) {
            is Long -> flowSettings.getLongFlow(key, defaultValue) as Flow<T>
            is String -> flowSettings.getStringFlow(key, defaultValue) as Flow<T>
            is Int -> flowSettings.getIntFlow(key, defaultValue) as Flow<T>
            is Boolean -> flowSettings.getBooleanFlow(key, defaultValue) as Flow<T>
            is Float -> flowSettings.getFloatFlow(key, defaultValue) as Flow<T>
            else -> throw UnsupportedPersistenceException()
        }
}
