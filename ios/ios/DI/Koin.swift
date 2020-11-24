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
        get {
            return self[KoinKey.self]
        }
        set {
            self[KoinKey.self] = newValue
        }
    }
}

// swiftlint:disable force_cast
extension Koin {
    func getMainViewModel() -> MainViewModel {
        return koin.getForIOS(objCClass: MainViewModel.self) as! MainViewModel
    }
}
