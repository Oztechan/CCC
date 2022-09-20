package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.util.removeScientificNotation
import com.oztechan.ccc.common.model.Watcher
import com.oztechan.ccc.client.model.Watcher as WatcherUIModel

internal fun Watcher.toUIModel() = WatcherUIModel(
    id = id,
    base = base,
    target = target,
    isGreater = isGreater,
    rate = rate.removeScientificNotation()
)

internal fun List<Watcher>.toUIModelList() = map {
    it.toUIModel()
}
