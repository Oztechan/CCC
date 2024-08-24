//
//  Application.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import FirebaseCore
import GoogleMobileAds
import Provider
import SwiftUI

var logger: KermitLogger = {
    return LoggerKt.doInitLogger(isCrashlyticsEnabled: EnvironmentUtil.isRelease)
}()

@main
struct Application: App {
    init() {
        if EnvironmentUtil.isRelease {
            FirebaseApp.configure()
        }

        logger.i(message: { "Application init" })

        GADMobileAds.sharedInstance().start(completionHandler: nil)
        GADMobileAds.sharedInstance().applicationMuted = true
        GADMobileAds.sharedInstance().applicationVolume = 0
    }

    var body: some Scene {
        MainRootView()
    }
}
