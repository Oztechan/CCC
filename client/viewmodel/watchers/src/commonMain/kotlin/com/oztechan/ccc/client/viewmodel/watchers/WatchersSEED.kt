package com.oztechan.ccc.client.viewmodel.watchers

import com.oztechan.ccc.client.core.viewmodel.BaseData
import com.oztechan.ccc.client.core.viewmodel.BaseEffect
import com.oztechan.ccc.client.core.viewmodel.BaseEvent
import com.oztechan.ccc.client.core.viewmodel.BaseState
import com.oztechan.ccc.common.core.model.Watcher

data class WatchersState(
    val watcherList: List<Watcher> = emptyList()
) : BaseState()

sealed class WatchersEffect : BaseEffect() {
    object Back : WatchersEffect()
    data class SelectBase(val watcher: Watcher) : WatchersEffect()
    data class SelectTarget(val watcher: Watcher) : WatchersEffect()
    object TooBigNumber : WatchersEffect()
    object InvalidInput : WatchersEffect()
    object MaximumNumberOfWatchers : WatchersEffect()
}

interface WatchersEvent : BaseEvent {
    fun onBackClick()
    fun onBaseClick(watcher: Watcher)
    fun onTargetClick(watcher: Watcher)
    fun onBaseChanged(watcher: Watcher, newBase: String)
    fun onTargetChanged(watcher: Watcher, newTarget: String)
    fun onAddClick()
    fun onDeleteClick(watcher: Watcher)
    fun onRelationChange(watcher: Watcher, isGreater: Boolean)
    fun onRateChange(watcher: Watcher, rate: String): String
}

class WatchersData : BaseData() {
    companion object {
        const val MAXIMUM_INPUT = 9
        const val MAXIMUM_NUMBER_OF_WATCHER = 5
    }
}
