package com.oztechan.ccc.client.configservice.update.mapper

import com.oztechan.ccc.client.configservice.update.model.UpdateConfig as UpdateConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.UpdateConfig as UpdateConfigRCModel

internal fun UpdateConfigRCModel.toUpdateConfigModel() = UpdateConfigModel(
    updateLatestVersion = updateLatestVersion,
    updateForceVersion = updateForceVersion
)
