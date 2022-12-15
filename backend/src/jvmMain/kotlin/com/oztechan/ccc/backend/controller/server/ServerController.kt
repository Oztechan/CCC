package com.oztechan.ccc.backend.controller.server

internal interface ServerController {
    suspend fun getOfflineCurrencyResponseByBase(base: String): String?
}
