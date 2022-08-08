//
//  EnvironmentUtil.swift
//  CCC
//
//  Created by Mustafa Ozhan on 08.08.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

public struct EnvironmentUtil {
    public static var isRelease: Bool {
        #if RELEASE
        return true
        #else
        return false
        #endif
    }
}
