package com.oztechan.ccc.android.ui.mobile.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import co.touchlab.kermit.Logger

@Suppress("TooGenericExceptionCaught")
fun Context.resolveAndStartIntent(intent: Intent) {
    try {
        intent.resolveActivity(packageManager)?.let {
            startActivity(intent)
            null
        } ?: Exception("No activity found to handle the intent: $intent")
    } catch (e: ActivityNotFoundException) {
        Exception("Unable to open link", e)
    } catch (e: Exception) {
        Exception("An error occurred", e)
    }.let {
        Logger.e(it) { it.message.orEmpty() }
    }
}
