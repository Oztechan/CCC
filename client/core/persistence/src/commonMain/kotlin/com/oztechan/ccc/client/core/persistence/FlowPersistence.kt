package com.oztechan.ccc.client.core.persistence

import kotlinx.coroutines.flow.Flow

interface FlowPersistence {
    fun <T : Any> getFlow(key: String, defaultValue: T): Flow<T>
}
