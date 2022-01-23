package com.github.mustafaozhan.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Store(
    @SerialName("name") val name: String,
    @SerialName("update_dialog_title") val updateDialogTitle: String,
    @SerialName("update_dialog_description") val updateDialogDescription: String,
    @SerialName("update_force_version") val updateForceVersion: Int,
    @SerialName("update_latest_version") val updateLatestVersion: Int,
    @SerialName("store_url") val storeUrl: String
)
