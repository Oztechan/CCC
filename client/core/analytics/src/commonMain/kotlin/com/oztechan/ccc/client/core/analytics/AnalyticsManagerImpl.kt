package com.oztechan.ccc.client.core.analytics

import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.core.analytics.model.UserProperty
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.FirebaseAnalyticsEvents
import dev.gitlive.firebase.analytics.FirebaseAnalyticsParam
import dev.gitlive.firebase.analytics.analytics

internal class AnalyticsManagerImpl : AnalyticsManager {
    private val firebaseAnalytics by lazy { Firebase.analytics }

    init {
        firebaseAnalytics.setAnalyticsCollectionEnabled(true)
    }

    override fun trackScreen(screenName: ScreenName) {
        firebaseAnalytics.logEvent(
            FirebaseAnalyticsEvents.SCREEN_VIEW,
            buildMap<String, String> {
                FirebaseAnalyticsParam.SCREEN_NAME to screenName.getScreenName()
                FirebaseAnalyticsParam.SCREEN_CLASS to screenName.getScreenName()
            }
        )
    }

    override fun setUserProperty(userProperty: UserProperty) {
        firebaseAnalytics.setUserProperty(userProperty.key, userProperty.value)
    }

    override fun trackEvent(event: Event) {
        firebaseAnalytics.logEvent(event.key, event.getParams())
    }
}
