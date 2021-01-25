//
//  Extensions.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import client
import SwiftUI

extension ResourcesStringResource {
    func get() -> String {
        return ResourcesKt.getString(stringResource: self).localized()
    }
    func get(parameter: Any) -> String {
        return ResourcesKt.getString(stringResource: self, parameter: parameter).localized()
    }
}

extension ResourcesColorResource {
    func get() -> Color {
        return Color(ResourcesKt.getColor(colorResource: self))
    }
    func get() -> UIColor {
        return ResourcesKt.getColor(colorResource: self)
    }
}

extension ResourcesImageResource {
    func get() -> UIImage {
        return self.toUIImage()!
    }
}

extension String {
    func getImage() -> UIImage {
        return ResourcesKt.getDrawableByFileName(name: self).toUIImage()!
    }
}
