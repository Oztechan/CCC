package com.mustafaozhan.github.analytics

import com.mustafaozhan.github.analytics.model.EventParam
import com.mustafaozhan.github.analytics.model.FirebaseEvent
import com.mustafaozhan.github.analytics.model.UserProperty

interface AnalyticsManager {
    fun trackScreen(screenName: String)

    fun setUserProperty(userProperty: UserProperty, value: String)

    fun trackEvent(event: FirebaseEvent, params: Map<EventParam, String>? = null)
}
