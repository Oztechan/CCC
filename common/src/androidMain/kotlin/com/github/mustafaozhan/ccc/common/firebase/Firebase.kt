/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.firebase

import android.content.Context
import com.github.mustafaozhan.ccc.common.BuildConfig
import com.github.mustafaozhan.ccc.common.handler.WatchDogHandler
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun initFirebase(
    context: Context,
    enableAnalytics: Boolean = false
) {

    if (!BuildConfig.DEBUG && enableAnalytics) {
        FirebaseAnalytics.getInstance(context)
    }

    FirebaseCrashlytics
        .getInstance()
        .setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

    Thread.setDefaultUncaughtExceptionHandler(WatchDogHandler())
}
