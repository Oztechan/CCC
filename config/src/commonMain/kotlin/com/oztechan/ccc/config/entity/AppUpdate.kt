package com.oztechan.ccc.config.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AppUpdate(
    @SerialName("name") val name: String,
    @SerialName("update_latest_version") val updateLatestVersion: Int,
    @SerialName("update_force_version") val updateForceVersion: Int
)
