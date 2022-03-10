package com.oztechan.ccc.common.settings

interface SettingsRepository {
    var firstRun: Boolean

    var currentBase: String

    var appTheme: Int

    var adFreeEndDate: Long

    var sessionCount: Long
}
