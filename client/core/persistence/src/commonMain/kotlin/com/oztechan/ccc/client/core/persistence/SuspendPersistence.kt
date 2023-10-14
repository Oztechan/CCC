package com.oztechan.ccc.client.core.persistence

interface SuspendPersistence {
    suspend fun <T : Any> getSuspend(key: String, defaultValue: T): T
    suspend fun <T : Any> setSuspend(key: String, value: T)
}
