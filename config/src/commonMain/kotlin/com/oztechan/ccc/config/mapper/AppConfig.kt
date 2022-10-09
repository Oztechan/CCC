package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.entity.AppConfig as AppConfigEntity
import com.oztechan.ccc.config.model.AppConfig as AppConfigModel

internal fun AppConfigEntity.toModel() = AppConfigModel(
    adConfig = adConfig.toModel(),
    appReview = appReview.toModel(),
    appUpdate = appUpdate.map { it.toModel() }
)
