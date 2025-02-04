package com.oztechan.ccc.client.core.analytics

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.core.analytics.model.UserProperty

internal class AnalyticsManagerImpl : AnalyticsManager {
    private val firebaseAnalytics by lazy { Firebase.analytics }

    override fun trackScreen(screenName: ScreenName) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName.getScreenName())
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName.getScreenName())
        }
    }

    override fun setUserProperty(userProperty: UserProperty) {
        firebaseAnalytics.setUserProperty(userProperty.key, userProperty.value)
    }

    override fun trackEvent(event: Event) {
        firebaseAnalytics.logEvent(event.key) {
            event.getParams()
                ?.iterator()
                ?.forEach {
                    param(it.key, it.value)
                }
        }
    }
}
