package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.util.removeScientificNotation
import com.oztechan.ccc.common.model.Notification
import com.oztechan.ccc.client.model.Notification as NotificationUIModel

fun Notification.toUIModel() = NotificationUIModel(
    id = id,
    base = base,
    target = target,
    isGreater = isGreater,
    rate = rate.removeScientificNotation()
)

fun List<Notification>.toUIModelList() = map {
    it.toUIModel()
}