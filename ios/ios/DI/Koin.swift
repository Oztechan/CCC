//
//  Koin.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import client

struct Koin {
    let koin = KoinIOSKt.doInitIOS(
        userDefaults: UserDefaults(suiteName: "application_user_defaults")!
    ).koin
}

struct KoinKey: EnvironmentKey {
    typealias Value = Koin
    static var defaultValue = Koin()
}

extension EnvironmentValues {
    var koin: Koin {
        set {
            self[KoinKey.self] = newValue
        }
        // swiftlint:disable implicit_getter
        get {
            return self[KoinKey.self]
        }
    }
}

// swiftlint:disable force_cast
extension Koin {
    func get() -> MainViewModel {
        return koin.getDependency(objCClass: MainViewModel.self) as! MainViewModel
    }

    func get() -> CalculatorViewModel {
        return koin.getDependency(objCClass: CalculatorViewModel.self) as! CalculatorViewModel
    }

    func get() -> CurrenciesViewModel {
        return koin.getDependency(objCClass: CurrenciesViewModel.self) as! CurrenciesViewModel
    }
}
