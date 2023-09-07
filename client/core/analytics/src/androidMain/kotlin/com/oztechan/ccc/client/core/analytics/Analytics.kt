package com.oztechan.ccc.client.core.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

fun initAnalytics(context: Context) {
    FirebaseAnalytics.getInstance(context)
}
