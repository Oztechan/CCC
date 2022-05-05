package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.model.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.oztechan.ccc.common.db.sql.Notification as NotificationEntity

fun NotificationEntity.toModel() = Notification(
    id = id,
    base = base,
    target = target,
    isGreater = isGreater.toBoolean(),
    rate = rate,
)

internal fun List<NotificationEntity>.toModelList(): List<Notification> {
    return map { it.toModel() }
}

internal fun Flow<List<NotificationEntity>>.mapToModel(): Flow<List<Notification>> {
    return this.map { it.toModelList() }
}