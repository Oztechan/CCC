package com.oztechan.ccc.client.storage.app

interface AppStorage {
    suspend fun isFirstRun(): Boolean
    suspend fun setFirstRun(value: Boolean)

    suspend fun getAppTheme(): Int
    suspend fun setAppTheme(value: Int)

    suspend fun getPremiumEndDate(): Long
    suspend fun setPremiumEndDate(value: Long)

    suspend fun getSessionCount(): Long
    suspend fun setSessionCount(value: Long)
}
