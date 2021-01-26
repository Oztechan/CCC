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

    @StateObject var mainVMWrapper: MainVMWrapper = startKoin().get()

    init() {
        LoggerKt.kermit.d(withMessage: {"CCCApp init"})
    }

    var body: some Scene {
        WindowGroup {
            if mainVMWrapper.viewModel.isFistRun() {
                CurrenciesView(currenciesNavigationToogle: .constant(false))
            } else {
                CalculatorView()
            }
        }
    }
}
