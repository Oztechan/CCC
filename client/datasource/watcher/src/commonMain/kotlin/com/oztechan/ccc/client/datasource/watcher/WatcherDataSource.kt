package com.oztechan.ccc.client.datasource.watcher

import com.oztechan.ccc.common.core.model.Watcher
import kotlinx.coroutines.flow.Flow

interface WatcherDataSource {
    fun getWatchersFlow(): Flow<List<Watcher>>
    suspend fun addWatcher(base: String, target: String)
    suspend fun getWatchers(): List<Watcher>
    suspend fun deleteWatcher(id: Long)
    suspend fun updateWatcherBaseById(base: String, id: Long)
    suspend fun updateWatcherTargetById(target: String, id: Long)
    suspend fun updateWatcherRelationById(isGreater: Boolean, id: Long)
    suspend fun updateWatcherRateById(rate: Double, id: Long)
}
