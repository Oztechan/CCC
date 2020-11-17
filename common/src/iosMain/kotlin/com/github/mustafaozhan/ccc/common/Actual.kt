/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import co.touchlab.kermit.CommonLogger
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import platform.UIKit.UIDevice

actual val platformName = UIDevice.currentDevice.systemName()
actual val platformVersion = UIDevice.currentDevice.systemVersion

actual class PlatformLogger : Logger() {
    private val logger = CommonLogger()
    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        logger.log(severity, message, tag, throwable)
    }
}
