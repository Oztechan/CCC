package com.oztechan.ccc.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

fun initAnalytics(context: Context) {
    FirebaseAnalytics.getInstance(context)
}
