package com.oztechan.ccc.client.viewmodel.watchers


import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.Watcher
import kotlinx.coroutines.flow.MutableStateFlow

data class WatchersState(
    val watcherList: List<Watcher> = emptyList(),
    val base: String = "",
    val target: String = ""
) : BaseState()

sealed class WatchersEffect : BaseEffect() {
    object Back : WatchersEffect()
    data class SelectBase(val watcher: Watcher) : WatchersEffect()
    data class SelectTarget(val watcher: Watcher) : WatchersEffect()
    object MaximumInput : WatchersEffect()
    object InvalidInput : WatchersEffect()
    object MaximumNumberOfWatchers : WatchersEffect()
}

interface WatchersEvent : BaseEvent {
    fun onBackClick()
    fun onBaseClick(watcher: Watcher)
    fun onTargetClick(watcher: Watcher)
    fun onBaseChanged(watcher: Watcher?, newBase: String)
    fun onTargetChanged(watcher: Watcher?, newTarget: String)
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

// Extension
fun MutableStateFlow<WatchersState>.update(
    watcherList: List<Watcher> = value.watcherList,
) {
    value = value.copy(
        watcherList = watcherList
    )
}