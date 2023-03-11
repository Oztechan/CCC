package com.oztechan.ccc.client.core.analytics

import com.oztechan.ccc.client.core.analytics.model.Event
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import com.oztechan.ccc.client.core.analytics.model.UserProperty

interface AnalyticsManager {
    fun trackScreen(screenName: ScreenName)

    fun setUserProperty(userProperty: UserProperty)

    fun trackEvent(event: Event)
}
