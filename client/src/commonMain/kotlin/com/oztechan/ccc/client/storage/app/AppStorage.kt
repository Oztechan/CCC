package com.oztechan.ccc.client.storage.app

interface AppStorage {
    var firstRun: Boolean

    var appTheme: Int

    var adFreeEndDate: Long

    var sessionCount: Long
}
