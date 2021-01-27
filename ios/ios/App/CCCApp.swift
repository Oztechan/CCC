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
