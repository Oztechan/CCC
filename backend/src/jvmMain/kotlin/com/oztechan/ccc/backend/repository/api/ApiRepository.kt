package com.oztechan.ccc.backend.repository.api

interface ApiRepository {
    fun startSyncApi()
    suspend fun getOfflineCurrencyResponseByBase(base: String): String?
}
