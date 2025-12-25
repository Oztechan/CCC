package com.oztechan.ccc.backend.controller.sync

import kotlin.time.Duration

interface SyncController {
    suspend fun syncPrimaryCurrencies(delayDuration: Duration)
    suspend fun syncSecondaryCurrencies(delayDuration: Duration)
    suspend fun syncTertiaryCurrencies(delayDuration: Duration)
    suspend fun syncUnPopularCurrencies(delayDuration: Duration)
}
