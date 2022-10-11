//
//  AnalyticsManagerImpl.swift
//  CCC
//
//  Created by Mustafa Ozhan on 10.08.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Provider
import FirebaseAnalytics

class AnalyticsManagerImpl: AnalyticsManager {

    func setUserProperty(userProperty: UserProperty) {
        Analytics.setUserProperty(userProperty.value, forName: userProperty.key)
    }

    func trackEvent(event: Event) {
        if event.getParams() == nil {
            Analytics.logEvent(event.key, parameters: nil)
        } else {

            var analyticsParams = [String: String]()
            for (key, value) in event.getParams()! {
                analyticsParams[key] = value
            }

            Analytics.logEvent(event.key, parameters: analyticsParams)
        }
    }

    func trackScreen(screenName: ScreenName) {
        Analytics.logEvent(
            AnalyticsEventScreenView,
            parameters: [
                AnalyticsParameterScreenName: screenName.getScreenName(),
                AnalyticsParameterScreenClass: screenName.getScreenName()
            ]
        )
    }
}
