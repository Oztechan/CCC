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
            return 34.cp()
        case .title:
            return 28.cp()
        case .title2:
            return 22.cp()
        case .title3:
            return 20.cp()
        case .headline:
            return 17.cp()
        case .subheadline:
            return 15.cp()
        case .body:
            return 17.cp()
        case .callout:
            return 16.cp()
        case .footnote:
            return 13.cp()
        case .caption:
            return 12.cp()
        case .caption2:
            return 11.cp()
        @unknown default:
            fatalError("Expected to have a valid style")
        }
    }
}

extension Double {
    // clever pixel
    func cp() -> Double {
        if UIDevice.current.userInterfaceIdiom == .pad {
            return self * 2 / 3 + self
        } else {
            return self
        }
    }
}

extension Image {
    func resize(width: Double, height: Double) -> some View {
        return self
            .resizable()
            .scaledToFit()
            .frame(width: width, height: height, alignment: .center)
    }

    func resize(widthAndHeight: Double) -> some View {
        return self.resize(
            width: widthAndHeight,
            height: widthAndHeight
        )
    }
}
