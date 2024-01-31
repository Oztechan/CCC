package com.oztechan.ccc.client.storage.app

interface AppStorage {
    suspend fun isFirstRun(): Boolean
    suspend fun setFirstRun(value: Boolean)

    suspend fun getAppTheme(): Int
    suspend fun setAppTheme(value: Int)

    var premiumEndDate: Long

    var sessionCount: Long
}
