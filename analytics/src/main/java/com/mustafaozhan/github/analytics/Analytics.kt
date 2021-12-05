package com.mustafaozhan.github.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

fun initAnalytics(context: Context) {
    FirebaseAnalytics.getInstance(context)
}
