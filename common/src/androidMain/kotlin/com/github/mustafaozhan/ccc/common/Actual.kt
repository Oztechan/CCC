/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import co.touchlab.kermit.CommonLogger
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.github.mustafaozhan.ccc.common.error.NonThrowableException
import com.google.firebase.crashlytics.FirebaseCrashlytics

actual val platformName by lazy { "Android" }
actual val platformVersion = android.os.Build.VERSION.SDK_INT.toString()

actual class PlatformLogger : Logger() {
    private val logger = CommonLogger()

    companion object {
        private const val CRASHLYTICS_KEY_PRIORITY = "priority"
        private const val CRASHLYTICS_KEY_TAG = "tag"
        private const val CRASHLYTICS_KEY_MESSAGE = "message"
    }

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            logger.log(
                severity,
                "@${Thread.currentThread().name}: $message",
                tag,
                throwable
            )
        } else {

            if (severity == Severity.Verbose || severity == Severity.Debug || severity == Severity.Info) {
                return
            }

            FirebaseCrashlytics.getInstance().apply {

                setCustomKey(CRASHLYTICS_KEY_PRIORITY, severity.name)
                setCustomKey(CRASHLYTICS_KEY_TAG, tag)
                setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)
                recordException(throwable ?: NonThrowableException(message))
            }
        }
    }
}
