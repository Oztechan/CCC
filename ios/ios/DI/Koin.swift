//
//  Koin.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client

// swiftlint:disable force_cast
class Koin {
    static let shared: Koin = Koin()

    let koin = KoinIOSKt.doInitIOS(
        userDefaults: UserDefaults(suiteName: "application_user_defaults")!
    ).koin

    lazy var mainVMWrapper: MainVMWrapper = {
        return MainVMWrapper(
            viewModel: Koin.shared.koin.getDependency(objCClass: MainViewModel.self) as! MainViewModel
        )
    }()

    lazy var calculatorVMWrapper: CalculatorVMWrapper = {
        return CalculatorVMWrapper(
            viewModel: Koin.shared.koin.getDependency(objCClass: CalculatorViewModel.self) as! CalculatorViewModel
        )
    }()

    lazy var currenciesVMWrapper: CurrenciesVMWrapper = {
        return CurrenciesVMWrapper(
            viewModel: Koin.shared.koin.getDependency(objCClass: CurrenciesViewModel.self) as! CurrenciesViewModel
        )
    }()

    lazy var barVMWrapper: BarVMWrapper = {
        return BarVMWrapper(
            viewModel: Koin.shared.koin.getDependency(objCClass: BarViewModel.self) as! BarViewModel
        )
    }()

    lazy var settingsVMWrapper: SettingsVMWrapper = {
        return SettingsVMWrapper(
            viewModel: Koin.shared.koin.getDependency(objCClass: SettingsViewModel.self) as! SettingsViewModel
        )
    }()
}
