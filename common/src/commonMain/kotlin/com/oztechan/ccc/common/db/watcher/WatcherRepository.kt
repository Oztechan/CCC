package com.oztechan.ccc.common.db.watcher

import com.oztechan.ccc.common.model.Watcher
import kotlinx.coroutines.flow.Flow

interface WatcherRepository {
    fun addWatcher(base: String, target: String)
    fun collectWatchers(): Flow<List<Watcher>>
    fun getWatchers(): List<Watcher>
    fun deleteWatcher(id: Long)
    fun updateBaseById(base: String, id: Long)
    fun updateTargetById(target: String, id: Long)
    fun updateRelationById(isGreater: Boolean, id: Long)
    fun updateRateById(rate: Double, id: Long)
}