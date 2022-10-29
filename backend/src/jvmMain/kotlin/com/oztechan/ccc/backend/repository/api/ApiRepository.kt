package com.oztechan.ccc.backend.repository.api

internal interface ApiRepository {
    fun startSyncApi()
    suspend fun getOfflineCurrencyResponseByBase(base: String): String?
}
