package com.oztechan.ccc.android.ui.mobile.util

import android.content.Context
import android.content.Intent
import co.touchlab.kermit.Logger

fun Context.resolveAndStartIntent(intent: Intent) {
    intent.resolveActivity(packageManager)?.let {
        startActivity(intent)
    } ?: Logger.w { "No activity found to handle the intent: $intent" }
}
