package com.oztechan.ccc.client.core.analytics

import android.content.Context
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

fun initAnalytics(context: Context) {
    Firebase.initialize(context)
}
