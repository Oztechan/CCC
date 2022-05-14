package com.oztechan.ccc.client.viewmodel.notification


import com.oztechan.ccc.client.base.BaseEffect
import com.oztechan.ccc.client.base.BaseEvent
import com.oztechan.ccc.client.base.BaseState
import com.oztechan.ccc.client.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow

data class NotificationState(
    val notificationList: List<Notification> = emptyList(),
    val base: String = "",
    val target: String = ""
) : BaseState()

sealed class NotificationEffect : BaseEffect() {
    object Back : NotificationEffect()
    data class SelectBase(val notification: Notification) : NotificationEffect()
    data class SelectTarget(val notification: Notification) : NotificationEffect()
}

interface NotificationEvent : BaseEvent {
    fun onBackClick()
    fun onBaseClick(notification: Notification)
    fun onTargetClick(notification: Notification)
    fun onBaseChanged(notification: Notification?, newBase: String)
    fun onTargetChanged(notification: Notification?, newTarget: String)
    fun onAddClick()
    fun onDeleteClick(notification: Notification)
    fun onRelationChange(notification: Notification, isGreater: Boolean)
    fun onRateChange(notification: Notification, rate: String)
}

// Extension
fun MutableStateFlow<NotificationState>.update(
    notificationList: List<Notification> = value.notificationList,
) {
    value = value.copy(
        notificationList = notificationList
    )
}