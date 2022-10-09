package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.entity.AppUpdate as AppUpdateEntity
import com.oztechan.ccc.config.model.AppUpdate as AppUpdateModel

internal fun AppUpdateEntity.toModel() = AppUpdateModel(
    name = name,
    updateLatestVersion = updateLatestVersion,
    updateForceVersion = updateForceVersion
)
