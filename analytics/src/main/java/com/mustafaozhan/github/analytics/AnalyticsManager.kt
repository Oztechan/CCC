package com.mustafaozhan.github.analytics

import com.mustafaozhan.github.analytics.model.UserProperty

interface AnalyticsManager {
    fun trackScreen(screenName: String)
    fun setUserProperty(userProperty: UserProperty, value: String)
}
