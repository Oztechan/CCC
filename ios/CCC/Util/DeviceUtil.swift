//
//  DeviceUtil.swift
//  CCC
//
//  Created by Mustafa Ozhan on 30.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit

public struct DeviceUtil {
    public static func getBottomNotchHeight() -> Double {
        let keyWindow = UIApplication.shared.connectedScenes
            .filter({ $0.activationState == .foregroundActive })
            .map({ $0 as? UIWindowScene })
            .compactMap({ $0 })
            .first?.windows
            .filter({ $0.isKeyWindow }).first

        return Double(keyWindow?.safeAreaInsets.bottom ?? 0)
    }
}
