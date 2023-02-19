package com.oztechan.ccc.client.configservice.update.mapper

import com.oztechan.ccc.client.configservice.update.UpdateConfig as UpdateConfigRCModel
import com.oztechan.ccc.client.configservice.update.model.UpdateConfig as UpdateConfigModel

internal fun UpdateConfigRCModel.toUpdateConfigModel() = UpdateConfigModel(
    updateLatestVersion = updateLatestVersion,
    updateForceVersion = updateForceVersion
)
