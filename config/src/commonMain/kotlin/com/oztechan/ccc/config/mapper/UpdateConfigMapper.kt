package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.config.model.UpdateConfig as UpdateConfigModel
import com.oztechan.ccc.config.service.update.UpdateConfig as UpdateConfigRCModel

internal fun UpdateConfigRCModel.toUpdateConfigModel() = UpdateConfigModel(
    updateLatestVersion = updateLatestVersion,
    updateForceVersion = updateForceVersion
)
