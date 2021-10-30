package com.github.mustafaozhan.ccc.common.util

import co.touchlab.kermit.Logger
import io.ktor.client.features.logging.Logger as LoggerKtor

class KtorLogger : LoggerKtor {
    override fun log(message: String) {
        if (message.contains(KTOR_LOG_MASK_REQUEST)
            || message.contains(KTOR_LOG_MASK_RESPONSE)
        ) {
            Logger.v { "$TAG: $message" }
        }
    }

    companion object {
        private const val TAG = "KTOR"
        private const val KTOR_LOG_MASK_REQUEST = "REQUEST"
        private const val KTOR_LOG_MASK_RESPONSE = "RESPONSE"
    }
}
