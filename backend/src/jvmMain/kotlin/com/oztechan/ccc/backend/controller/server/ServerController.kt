package com.oztechan.ccc.backend.controller.server

internal interface ServerController {
    suspend fun getExchangeRateTextByBase(base: String): String?
}
