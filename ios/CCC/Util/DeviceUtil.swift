//
//  DeviceUtil.swift
//  CCC
//
//  Created by Mustafa Ozhan on 30.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit

struct DeviceUtil {
    public static func getBottomNotchHeight() -> Double {
        return Double(
            UIApplication.shared.windows.first?.safeAreaInsets.bottom ?? 0
        )
    }
}
