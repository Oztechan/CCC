package com.oztechan.ccc.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.oztechan.ccc.analytics.model.Event
import com.oztechan.ccc.analytics.model.ScreenName
import com.oztechan.ccc.analytics.model.UserProperty
import com.oztechan.ccc.analytics.util.isDeviceRooted

internal class AnalyticsManagerImpl(
    context: Context
) : AnalyticsManager {
    private val firebaseAnalytics by lazy { Firebase.analytics }

    init {
        setUserProperty(UserProperty.IsRooted(isDeviceRooted(context)))
    }

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
            event.getParams()?.forEach {
                param(it.key, it.value)
            }
        }
    }
}
