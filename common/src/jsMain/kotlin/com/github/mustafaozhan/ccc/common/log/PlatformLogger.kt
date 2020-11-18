/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.log

import co.touchlab.kermit.CommonLogger
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

actual class PlatformLogger : Logger() {
    private val logger = CommonLogger()
    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        logger.log(severity, message, tag, throwable)
    }
}
