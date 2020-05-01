/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import mustafaozhan.github.com.mycurrencies.BuildConfig

@JsonClass(generateAdapter = true)
data class RemoteConfig(
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "update_url") val updateUrl: String,
    @Json(name = "force_version") val forceVersion: Int = BuildConfig.VERSION_CODE,
    @Json(name = "latest_version") val latestVersion: Int = BuildConfig.VERSION_CODE
)
