package com.oztechan.ccc.ios.provider

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initCrashlytics
import com.github.submob.logmob.initLogger

@Suppress("unused")
fun initLogger(): Logger {
    // should be before initCrashlytics
    initLogger()
    initCrashlytics()
    return Logger
}
