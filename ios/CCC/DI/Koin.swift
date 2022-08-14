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

    _koin = IOSKoinKt.doInitIOS(
        userDefaults: userDefaults,
        analyticsManager: AnalyticsManagerImpl()
    ).koin
}

private var _koin: Koin_coreKoin?

var koin: Koin_coreKoin {
    return _koin!
}

extension Koin_coreKoin {
    // swiftlint:disable force_cast
    func get<T>() -> T {
        return koin.getDependency(objCObject: T.self) as! T
    }
}
