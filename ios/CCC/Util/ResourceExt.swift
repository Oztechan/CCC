//
//  ResourceExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.11.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

extension ResourcesColorResource {
    func get() -> Color {
        return Color(get())
    }
    func get() -> UIColor {
        return Resources_iosKt.getColor(colorResource: self)
    }
}
