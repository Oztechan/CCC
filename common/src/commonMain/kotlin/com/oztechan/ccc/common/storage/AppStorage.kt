package com.oztechan.ccc.common.storage

interface AppStorage {
    var firstRun: Boolean

    var currentBase: String

    var appTheme: Int

    var adFreeEndDate: Long

    var sessionCount: Long

    var precision: Int
}
