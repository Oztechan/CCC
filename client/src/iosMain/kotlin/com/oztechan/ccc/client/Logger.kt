package com.oztechan.ccc.client

import co.touchlab.kermit.Logger
import com.github.submob.logmob.initCrashlytics
import com.github.submob.logmob.initLogger

@Suppress("unused")
fun initLogger(): Logger {
    initLogger()

    if (!Platform.isDebugBinary) {
        initCrashlytics()
    }

    return Logger
}
