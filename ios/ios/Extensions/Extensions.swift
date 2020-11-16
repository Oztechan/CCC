//
//  Extensions.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI

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
