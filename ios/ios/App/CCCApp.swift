//
//  CCCApp.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client
import common

@main
struct CCCApp: App {
    @Environment(\.koin) var koin: Koin
    
    init() {
        LoggerKt.kermit.d(withMessage: {"init app"})
    }

    var body: some Scene {
        WindowGroup {
            CalculatorView(manager: CalculatorManager(vm: koin.getCalculatorViewModel()))
        }
    }
}
