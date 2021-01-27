//
//  Koin.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client

func startKoin() -> Koin_coreKoin {
    let userDefaults = UserDefaults(suiteName: "application_user_defaults")!

    _koin = KoinIOSKt.doInitIOS(
        userDefaults: userDefaults
    ).koin

    return koin
}

private var _koin: Koin_coreKoin?

var koin: Koin_coreKoin {
    return _koin!
}

// swiftlint:disable force_cast
extension Koin_coreKoin {

    // viewmodel
    func get() -> MainViewModel {
        return koin.getDependency(objCClass: MainViewModel.self) as! MainViewModel
    }

    func get() -> CalculatorViewModel {
        return koin.getDependency(objCClass: CalculatorViewModel.self) as! CalculatorViewModel
    }

    func get() -> CurrenciesViewModel {
        return koin.getDependency(objCClass: CurrenciesViewModel.self) as! CurrenciesViewModel
    }

    func get() -> BarViewModel {
        return koin.getDependency(objCClass: BarViewModel.self) as! BarViewModel
    }

    func get() -> SettingsViewModel {
        return koin.getDependency(objCClass: SettingsViewModel.self) as! SettingsViewModel
    }

    // ObservableVM
    func get() -> MainObservable {
        return MainObservable(viewModel: get())
    }

    // ObservableSEED
    func get() -> CalculatorObservable {
        return CalculatorObservable(viewModel: get())
    }

    func get() -> BarObservable {
        return BarObservable(viewModel: get())
    }

    func get() -> SettingsObservable {
        return SettingsObservable(viewModel: get())
    }

    func get() -> CurrenciesObservable {
        return CurrenciesObservable(viewModel: get())
    }
}
