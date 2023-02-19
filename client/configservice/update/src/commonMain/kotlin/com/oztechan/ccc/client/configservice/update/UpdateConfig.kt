package com.oztechan.ccc.client.configservice.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateConfig(
    @SerialName("update_latest_version") val updateLatestVersion: Int = 0,
    @SerialName("update_force_version") val updateForceVersion: Int = 0
)
