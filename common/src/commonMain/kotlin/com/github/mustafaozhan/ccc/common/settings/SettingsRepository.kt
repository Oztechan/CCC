package com.github.mustafaozhan.ccc.common.settings

interface SettingsRepository {
    var firstRun: Boolean

    var currentBase: String

    var appTheme: Int

    var adFreeEndDate: Long

    var lastReviewRequest: Long
}
