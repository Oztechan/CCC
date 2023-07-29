package com.oztechan.ccc.client.core.remoteconfig.util

import co.touchlab.kermit.Logger
import kotlinx.serialization.json.Json

@Suppress("TooGenericExceptionCaught")
inline fun <reified T> String?.parseToObject(): T? = if (!isNullOrEmpty()) {
    try {
        Json.decodeFromString<T>(this)
    } catch (exception: Exception) {
        Logger.e(exception) { "Parsing exception" }
        null
    }
} else {
    Logger.e { "Not parse-able string" }
    null
}
