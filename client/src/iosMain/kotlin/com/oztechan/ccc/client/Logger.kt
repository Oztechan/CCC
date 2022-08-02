package com.oztechan.ccc.client

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initCrashlytics
import com.github.submob.logmob.initLogger

@Suppress("unused")
fun initLogger(enableCrashlytics: Boolean): Logger {
    initLogger()

    if (enableCrashlytics) {
        initCrashlytics()
    }

    return Logger
}
