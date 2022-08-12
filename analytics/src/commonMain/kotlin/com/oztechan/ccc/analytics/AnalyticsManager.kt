package com.oztechan.ccc.analytics

import com.oztechan.ccc.analytics.model.Event
import com.oztechan.ccc.analytics.model.ScreenName
import com.oztechan.ccc.analytics.model.UserProperty

interface AnalyticsManager {
    fun trackScreen(screenName: ScreenName)

    fun setUserProperty(userProperty: UserProperty)

    fun trackEvent(event: Event)
}
