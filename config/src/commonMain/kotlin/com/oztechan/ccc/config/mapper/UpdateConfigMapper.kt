package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.client.core.remoteconfig.model.UpdateConfig as UpdateConfigRCModel
import com.oztechan.ccc.config.model.UpdateConfig as UpdateConfigModel

internal fun UpdateConfigRCModel.toUpdateConfigModel() = UpdateConfigModel(
    updateLatestVersion = updateLatestVersion,
    updateForceVersion = updateForceVersion
)
