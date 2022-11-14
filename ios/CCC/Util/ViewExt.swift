//
//  ViewExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.11.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

extension View {
    func withClearBackground(color: Color) -> some View {
        #if swift(>=5.7)
            if #available(iOS 16.0, *) {
                return self.background(color).scrollContentBackground(.hidden)
            } else {
                return self.background(color)
            }
        #else
            return self.background(color)
        #endif
    }

    func padding(
        top: Double = 0,
        leading: Double = 0,
        bottom: Double = 0,
        trailing: Double = 0
    ) -> some View {
        return self.padding(
            EdgeInsets(
                top: top,
                leading: leading,
                bottom: bottom,
                trailing: trailing
            )
        )
    }

    func font(relative: Font.TextStyle, size: Double? = nil) -> some View {
        return self.font(
            .custom(
                "SanFrancisco",
                size: size ?? getSizeFromStyle(style: relative),
                relativeTo: relative
            )
        )
    }

    func font(size: Double) -> some View {
        return self.font(.system(size: size))
    }

    // swiftlint:disable cyclomatic_complexity
    private func getSizeFromStyle(style: Font.TextStyle) -> Double {
        switch style {
        case .largeTitle:
            return 34
        case .title:
            return 28
        case .title2:
            return 22
        case .title3:
            return 20
        case .headline:
            return 17
        case .subheadline:
            return 15
        case .body:
            return 17
        case .callout:
            return 16
        case .footnote:
            return 13
        case .caption:
            return 12
        case .caption2:
            return 11
        @unknown default:
            fatalError("Expected to have a valid style")
        }
    }
}
