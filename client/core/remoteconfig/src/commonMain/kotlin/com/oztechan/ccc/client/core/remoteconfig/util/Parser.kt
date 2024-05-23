package com.oztechan.ccc.client.core.remoteconfig.util

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.core.remoteconfig.error.NonParsableStringException
import kotlinx.serialization.json.Json

@Suppress("TooGenericExceptionCaught")
inline fun <reified T> String?.parseToObject(): T = try {
    Json.decodeFromString<T>(this.orEmpty())
} catch (exception: Exception) {
    throw NonParsableStringException(this, exception).also {
        Logger.e(it) { it.message.orEmpty() }
    }
}
