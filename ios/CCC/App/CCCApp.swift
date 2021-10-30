//
//  CCCApp.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import Client
import Firebase
import GoogleMobileAds

let logger = LoggerKt.doInitLogger()

@main
struct CCCApp: App {

    init() {
        logger.i(message: {"CCCApp init"})

        #if RELEASE
            FirebaseApp.configure()
        #endif

        GADMobileAds.sharedInstance().start(completionHandler: nil)

        startKoin()

        UITableView.appearance().tableHeaderView = UIView(frame: CGRect(
            x: 0,
            y: 0,
            width: 0,
            height: Double.leastNonzeroMagnitude
        ))
        UITableView.appearance().backgroundColor = MR.colors().transparent.get()
    }

    var body: some Scene {
        WindowGroup {
            MainView()
        }
    }
}
