package com.oztechan.ccc.android.ui.mobile.util

import android.content.Context
import android.content.Intent
import co.touchlab.kermit.Logger

fun Context.resolveAndStartIntent(intent: Intent) {
    intent.resolveActivity(packageManager)?.let {
        startActivity(intent)
    } ?: Exception("No activity found to handle the intent: $intent").let {
        Logger.e(it) { it.message.orEmpty() }
    }
}
