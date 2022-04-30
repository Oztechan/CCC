package com.oztechan.ccc.client.viewmodel.notifications


import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import kotlinx.coroutines.flow.MutableStateFlow

data class NotificationsState(
    val isEnabled: Boolean = false,
    val base: String = "",
    val target: String = ""
) : BaseState()

sealed class NotificationsEffect : BaseEffect() {
    object Back : NotificationsEffect()
    object SelectBase : NotificationsEffect()
    object SelectTarget : NotificationsEffect()
}

interface NotificationsEvent : BaseEvent {
    fun onBackClick()
    fun onBaseChange(base: String)
    fun onTargetChange(target: String)
    fun onBaseClick()
    fun onTargetClick()
    fun onStateClick()
}

// Extension
fun MutableStateFlow<NotificationsState>.update(
    isEnabled: Boolean = value.isEnabled,
    base: String = value.base,
    target: String = value.target
) {
    value = value.copy(
        isEnabled = isEnabled,
        base = base,
        target = target
    )
}