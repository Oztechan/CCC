package com.oztechan.ccc.common.db.notification

import com.oztechan.ccc.common.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun addNotification(base: String, target: String)
    fun collectNotifications(): Flow<List<Notification>>
}