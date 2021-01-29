//
//  CCCApp.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client

@main
struct CCCApp: App {

    init() {
        startKoin()
        LoggerKt.kermit.d(withMessage: {"CCCApp init"})
    }

    var body: some Scene {
        WindowGroup {
            MainView()
        }
    }
}
