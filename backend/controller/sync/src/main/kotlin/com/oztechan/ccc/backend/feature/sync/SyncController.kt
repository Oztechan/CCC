package com.oztechan.ccc.backend.feature.sync

interface SyncController {
    suspend fun syncPopularCurrencies()
    suspend fun syncUnPopularCurrencies()
}
