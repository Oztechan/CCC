package com.oztechan.ccc.common.datasource.watcher

import com.oztechan.ccc.common.model.Watcher
import kotlinx.coroutines.flow.Flow

interface WatcherDataSource {
    fun collectWatchers(): Flow<List<Watcher>>
    suspend fun addWatcher(base: String, target: String)
    suspend fun getWatchers(): List<Watcher>
    suspend fun deleteWatcher(id: Long)
    suspend fun updateBaseById(base: String, id: Long)
    suspend fun updateTargetById(target: String, id: Long)
    suspend fun updateRelationById(isGreater: Boolean, id: Long)
    suspend fun updateRateById(rate: Double, id: Long)
}
