package com.mustafaozhan.github.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.mustafaozhan.github.analytics.model.UserProperty
import com.mustafaozhan.github.analytics.util.getAvailableServices
import com.mustafaozhan.github.analytics.util.isDeviceRooted

class AnalyticsManagerImpl(
    context: Context
) : AnalyticsManager {
    private val firebaseAnalytics by lazy { Firebase.analytics }

    init {
        setDefaultUserProperties(context)
    }

    override fun trackScreen(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
    }

    override fun setUserProperty(userProperty: UserProperty, value: String) {
        firebaseAnalytics.setUserProperty(userProperty.key, value)
    }

    private fun setDefaultUserProperties(context: Context) {
        setUserProperty(UserProperty.MOBILE_SERVICES, getAvailableServices(context))
        setUserProperty(UserProperty.IS_ROOTED, isDeviceRooted(context))
    }
}
