package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.error.UnsupportedPersistenceException
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getFloatFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getStringFlow
import kotlinx.coroutines.flow.Flow

class FlowPersistenceImpl(private val observableSettings: ObservableSettings) : FlowPersistence {
    @Suppress("UNCHECKED_CAST", "OPT_IN_USAGE")
    override fun <T : Any> getFlow(key: String, defaultValue: T): Flow<T> =
        when (defaultValue) {
            is Long -> observableSettings.getLongFlow(key, defaultValue) as Flow<T>
            is String -> observableSettings.getStringFlow(key, defaultValue) as Flow<T>
            is Int -> observableSettings.getIntFlow(key, defaultValue) as Flow<T>
            is Boolean -> observableSettings.getBooleanFlow(key, defaultValue) as Flow<T>
            is Float -> observableSettings.getFloatFlow(key, defaultValue) as Flow<T>
            else -> throw UnsupportedPersistenceException()
        }
}
