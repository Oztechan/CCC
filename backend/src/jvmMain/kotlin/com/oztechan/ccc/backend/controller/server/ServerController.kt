package com.oztechan.ccc.backend.controller.server

internal interface ServerController {
    suspend fun getCurrencyResponseTextByBase(base: String): String?
}
