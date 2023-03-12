//
//  Koin.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Provider
import SwiftUI

var koin: Koin_coreKoin = {
    let userDefaults = UserDefaults(suiteName: "application_user_defaults")!

    return KoinKt.doInitKoin(
        userDefaults: userDefaults,
        analyticsManager: AnalyticsManagerImpl()
    ).koin
}()

extension Koin_coreKoin {
    func get<T>() -> T {
        // swiftlint:disable:next force_cast
        return koin.getDependency(objCObject: T.self) as! T
    }
}
