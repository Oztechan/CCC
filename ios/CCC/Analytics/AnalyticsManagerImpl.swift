//
//  AnalyticsManagerImpl.swift
//  CCC
//
//  Created by Mustafa Ozhan on 10.08.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Client
import FirebaseAnalytics

class AnalyticsManagerImpl: AnalyticsManager {
    func setUserProperty(userProperty: UserProperty, value: String) {
        Analytics.setUserProperty(value, forName: userProperty.key)
    }

    func trackEvent(event: Event, params: [EventParam: String]?) {
        if params != nil {
            var analyticsParams = [String: String]()
            for (key, value) in params! {
                analyticsParams[key.key] = value
        }

            Analytics.logEvent(event.key, parameters: analyticsParams)
        } else {
            Analytics.logEvent(event.key, parameters: nil)
        }
    }

    func trackScreen(screenName: String) {
        Analytics.logEvent(
            AnalyticsEventScreenView,
            parameters: [
                AnalyticsParameterScreenName: screenName,
                AnalyticsParameterScreenClass: screenName
            ]
        )
    }
}
