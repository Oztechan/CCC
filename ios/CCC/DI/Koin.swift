//
//  Koin.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import Client

func startKoin() {
    let userDefaults = UserDefaults(suiteName: "application_user_defaults")!

    _koin = KoinIOSKt.doInitIOS(
        userDefaults: userDefaults
    ).koin
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

    func get() -> SelectCurrencyViewModel {
        return koin.getDependency(objCClass: SelectCurrencyViewModel.self) as! SelectCurrencyViewModel
    }

    func get() -> SettingsViewModel {
        return koin.getDependency(objCClass: SettingsViewModel.self) as! SettingsViewModel
    }

    func get() -> WatchersViewModel {
        return koin.getDependency(objCClass: WatchersViewModel.self) as! WatchersViewModel
    }

    // Observable
    func get() -> MainObservable {
        return MainObservable(viewModel: get())
    }

    func get() -> CalculatorObservable {
        return CalculatorObservable(viewModel: get())
    }

    func get() -> SelectCurrencyObservable {
        return SelectCurrencyObservable(viewModel: get())
    }

    func get() -> SettingsObservable {
        return SettingsObservable(viewModel: get())
    }

    func get() -> WatchersObservable {
        return WatchersObservable(viewModel: get())
    }

    func get() -> CurrenciesObservable {
        return CurrenciesObservable(viewModel: get())
    }
}
