//
//  Resources.swift
//  ios
//
//  Created by Mustafa Ozhan on 21/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import client
import SwiftUI

extension ResourcesStringResource {
    func get() -> String {
        return ResourcesKt.getString(stringResource: self).localized()
    }
}

extension ResourcesColorResource {
    func get() -> Color {
        return Color(ResourcesKt.getColor(colorResource: self))
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
