package com.oztechan.ccc.client.core.persistence

import kotlinx.coroutines.flow.Flow

interface Persistence {
    fun <T : Any> getValue(key: String, defaultValue: T): T
    fun <T : Any> setValue(key: String, value: T)
    fun <T : Any> getFlow(key: String, defaultValue: T): Flow<T>
}
