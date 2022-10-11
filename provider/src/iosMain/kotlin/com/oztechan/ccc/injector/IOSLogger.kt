package com.oztechan.ccc.injector

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initCrashlytics
import com.github.submob.logmob.initLogger

@Suppress("unused")
fun initLogger(isCrashlyticsEnabled: Boolean): Logger {
    // should be before initCrashlytics
    initLogger()

    if (isCrashlyticsEnabled) {
        initCrashlytics()
    }
    return Logger
}
