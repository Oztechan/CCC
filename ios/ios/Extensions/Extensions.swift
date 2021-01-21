//
//  Extensions.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client
import UIKit

extension View {

    func getString(resource: ResourcesStringResource) -> String {
        return ResourcesKt.getString(stringResource: resource).localized()
    }

    func getImageByName(name: String) -> UIImage {
        return ResourcesKt.getDrawableByFileName(name: name).toUIImage()!
    }

    func getImage(resource: ResourcesImageResource) -> UIImage {
        return resource.toUIImage()!
    }

    func getColor(
        resource: ResourcesColorResource,
        scheme: ColorScheme
    ) -> Color {
        return Color(ResourcesKt.getColor(
            colorResource: resource,
            isDark: scheme == .dark
        ))
    }
}

#if DEBUG
struct PreviewProviderModifier: ViewModifier {

    var includeLightMode: Bool
    var includeDarkMode: Bool
    var includeRightToLeftMode: Bool
    var includeLargeTextMode: Bool

    func body(content: Content) -> some View {
        Group {
            if includeLightMode {
                content
                    .previewDisplayName("Light Mode")
                    .environment(\.colorScheme, .light)
            }

            if includeDarkMode {
                content
                    .previewDisplayName("Dark Mode")
                    .environment(\.colorScheme, .dark)
            }

            if includeRightToLeftMode {
                content
                    .previewDisplayName("Right To Left")
                    .environment(\.layoutDirection, .rightToLeft)
            }

            if includeLargeTextMode {
                content
                    .previewDisplayName("Large Text")
                    .environment(\.sizeCategory, .accessibilityExtraExtraLarge)
            }
        }
    }
}

extension View {
    func makeForPreviewProvider(
        includeLightMode: Bool = true,
        includeDarkMode: Bool = true,
        includeRightToLeftMode: Bool = true,
        includeLargeTextMode: Bool = true
    ) -> some View {
        modifier(
            PreviewProviderModifier(
                includeLightMode: includeLightMode,
                includeDarkMode: includeDarkMode,
                includeRightToLeftMode: includeRightToLeftMode,
                includeLargeTextMode: includeLargeTextMode
            )
        )
    }
}
#endif
