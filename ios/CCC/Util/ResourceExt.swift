//
//  ResourceExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.11.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

extension Image {
    init(resourceKey: KeyPath<Res.images, ResourcesImageResource>) {
        self.init(uiImage: Res.images()[keyPath: resourceKey].toUIImage()!)
    }

    init(imageName: String) {
        self.init(uiImage: ResourcesKt.getImageByName(name: imageName).toUIImage()!)
    }
}

extension String {
    init(_ resourceKey: KeyPath<Res.strings, ResourcesStringResource>) {
        self.init(
            Resources_iosKt.getString(
                stringResource: Res.strings()[keyPath: resourceKey]
            ).localized()
        )
    }

    init(_ resourceKey: KeyPath<Res.strings, ResourcesStringResource>, parameter: Any) {
        self.init(
            Resources_iosKt.getString(
                stringResource: Res.strings()[keyPath: resourceKey], parameter: parameter
            ).localized()
        )
    }
}

extension Color {
    init(_ resourceKey: KeyPath<Res.colors, ResourcesColorResource>) {
        self.init(
            Resources_iosKt.getColor(colorResource: Res.colors()[keyPath: resourceKey])
        )
    }
}

extension View {
    public func foregroundColor(
        _ resourceKey: KeyPath<Res.colors, ResourcesColorResource>
    ) -> some View {
        return self.foregroundColor(Color(resourceKey))
    }

    public func accentColor(
        _ resourceKey: KeyPath<Res.colors, ResourcesColorResource>
    ) -> some View {
        return self.accentColor(Color(resourceKey))
    }

    public func background(
        _ resourceKey: KeyPath<Res.colors, ResourcesColorResource>
    ) -> some View {
        return self.background(Color(resourceKey))
    }

    public func listRowBackground(
        _ resourceKey: KeyPath<Res.colors, ResourcesColorResource>
    ) -> some View {
        return self.listRowBackground(Color(resourceKey))
    }
}
