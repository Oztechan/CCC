package com.oztechan.ccc.backend.controller.client

interface ClientController {
    suspend fun syncPopularCurrencies()
    suspend fun syncUnPopularCurrencies()
}
