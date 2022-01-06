package com.github.mustafaozhan.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppUpdate(
    @SerialName("title") val title: String = "New Version Available",
    @SerialName("description") val description: String =
        "Looks like you have an older version of the app." +
            " Please update to get latest features and bug fixes to get best experience.",
    @SerialName("google_force_version") val googleForceVersion: Int = 716,
    @SerialName("google_latest_version") val googleLatestVersion: Int = 716,
    @SerialName("google_market_url") val googleMarketUrl: String =
        "https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies"
)
