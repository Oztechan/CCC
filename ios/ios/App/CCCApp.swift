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

    @ObservedObject var mainVMWrapper = MainVMWrapper()

    init() {
        LoggerKt.kermit.d(withMessage: {"CCCApp init"})
        mainVMWrapper.setViewModel(viewModel: koin.get())
    }

    var body: some Scene {
        WindowGroup {
            if mainVMWrapper.viewModel?.isFistRun() == true {
                CurrenciesView(viewModel: koin.get())
            } else {
                CalculatorView(viewModel: koin.get())
            }
        }
    }
}
