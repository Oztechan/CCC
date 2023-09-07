//
//  ResourceExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.11.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

extension ResourcesStringResource {
    func get() -> String {
        return Resources_iosKt.getString(stringResource: self).localized()
    }
    func get(parameter: Any) -> String {
        return Resources_iosKt.getString(stringResource: self, parameter: parameter).localized()
    }
}

extension ResourcesColorResource {
    func get() -> Color {
        return Color(get())
    }
    func get() -> UIColor {
        return Resources_iosKt.getColor(colorResource: self)
    }
}

extension ResourcesImageResource {
    func get() -> UIImage {
        return self.toUIImage()!
    }
}

extension String {
    func getImage() -> UIImage {
        return ResourcesKt.getImageByName(name: self).toUIImage()!
    }
}
