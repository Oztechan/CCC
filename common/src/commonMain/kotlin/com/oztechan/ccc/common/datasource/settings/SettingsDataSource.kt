package com.oztechan.ccc.common.datasource.settings

interface SettingsDataSource {
    var firstRun: Boolean

    var currentBase: String

    var appTheme: Int

    var adFreeEndDate: Long

    var sessionCount: Long

    var precision: Int
}
