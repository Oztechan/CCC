package com.oztechan.ccc.backend.controller.sync

interface SyncController {
    suspend fun syncPrimaryCurrencies()
    suspend fun syncSecondaryCurrencies()
    suspend fun syncTertiaryCurrencies()
    suspend fun syncUnPopularCurrencies()
}
