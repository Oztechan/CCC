/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import co.touchlab.kermit.CommonLogger
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

actual val platformName by lazy { "JVM" }
actual val platformVersion = KotlinVersion.CURRENT.toString()

actual class PlatformLogger : Logger() {
    private val logger = CommonLogger()
    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        logger.log(
            severity,
            "@${Thread.currentThread().name}: $message",
            tag,
            throwable
        )
    }
}
