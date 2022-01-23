package com.github.mustafaozhan.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppUpdate(
    @SerialName("name") val name: String,
    @SerialName("update_force_version") val updateForceVersion: Int,
    @SerialName("update_latest_version") val updateLatestVersion: Int,
)
