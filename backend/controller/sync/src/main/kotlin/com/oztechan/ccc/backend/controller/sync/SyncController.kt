package com.oztechan.ccc.backend.controller.sync

interface SyncController {
    suspend fun syncPopularCurrencies()
    suspend fun syncUnPopularCurrencies()
}
