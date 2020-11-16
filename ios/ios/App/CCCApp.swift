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
    @Environment(\.koin) var koin: Koin

    var body: some Scene {
        WindowGroup {
            MainView(
                mainViewModel: koin.getMainViewModel(),
                kermit: koin.getKermit()
            )
        }
    }
}
