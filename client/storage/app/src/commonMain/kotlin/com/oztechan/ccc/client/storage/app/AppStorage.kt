package com.oztechan.ccc.client.storage.app

interface AppStorage {
    var firstRun: Boolean

    suspend fun getAppTheme(): Int
    suspend fun setAppTheme(value: Int)

    var premiumEndDate: Long

    var sessionCount: Long
}
