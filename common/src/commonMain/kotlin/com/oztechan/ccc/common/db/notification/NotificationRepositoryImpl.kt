package com.oztechan.ccc.common.db.notification

import co.touchlab.kermit.Logger
import com.oztechan.ccc.common.db.sql.NotificationQueries
import com.oztechan.ccc.common.mapper.mapToModel
import com.oztechan.ccc.common.mapper.toLong
import com.oztechan.ccc.common.mapper.toModelList
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

class NotificationRepositoryImpl(
    private val notificationQueries: NotificationQueries
) : NotificationRepository {

    override fun addNotification(
        base: String,
        target: String
    ) = notificationQueries.addNotification(base, target)
        .also { Logger.v { "NotificationRepositoryImpl addNotification $base $target" } }

    override fun collectNotifications() = notificationQueries
        .getNotifications()
        .asFlow()
        .mapToList()
        .mapToModel()
        .also { Logger.v { "NotificationRepositoryImpl collectNotifications" } }

    override fun getNotifications() = notificationQueries
        .getNotifications()
        .executeAsList()
        .toModelList()
        .also { Logger.v { "NotificationRepositoryImpl collectNotifications" } }

    override fun deleteNotification(id: Long) = notificationQueries
        .deleteNotification(id)
        .also { Logger.v { "NotificationRepositoryImpl addNotification $id" } }

    override fun updateBaseById(base: String, id: Long) = notificationQueries
        .updateBaseById(base, id)
        .also { Logger.v { "NotificationRepositoryImpl updateBaseById $base $id" } }

    override fun updateTargetById(target: String, id: Long) = notificationQueries
        .updateTargetById(target, id)
        .also { Logger.v { "NotificationRepositoryImpl updateTargetById $target $id" } }

    override fun updateRelationById(isGreater: Boolean, id: Long) = notificationQueries
        .updateRelationById(isGreater.toLong(), id)
        .also { Logger.v { "NotificationRepositoryImpl updateRelationById $isGreater $id" } }

    override fun updateRateById(rate: Double, id: Long) = notificationQueries
        .updateRateById(rate, id)
        .also { Logger.v { "NotificationRepositoryImpl updateRateById $rate $id" } }
}
