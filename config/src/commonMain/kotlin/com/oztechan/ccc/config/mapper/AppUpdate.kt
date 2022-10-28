package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.model.UpdateConfig as AppUpdateModel
import com.oztechan.ccc.config.update.UpdateConfigEntity as AppUpdateEntity

internal fun AppUpdateEntity.toModel() = AppUpdateModel(
    updateLatestVersion = updateLatestVersion,
    updateForceVersion = updateForceVersion
)
