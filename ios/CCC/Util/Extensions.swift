//
//  Extensions.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Res
import Provider
import SwiftUI

extension ResourcesStringResource {
    func get() -> String {
        return IOSResourcesKt.getString(stringResource: self).localized()
    }
    func get(parameter: Any) -> String {
        return IOSResourcesKt.getString(stringResource: self, parameter: parameter).localized()
    }
}

extension ResourcesColorResource {
    func get() -> Color {
        return Color(get())
    }
    func get() -> UIColor {
        return IOSResourcesKt.getColor(colorResource: self)
    }
}

extension ResourcesImageResource {
    func get() -> UIImage {
        return self.toUIImage()!
    }
}

extension String {
    func getImage() -> UIImage {
        return IOSResourcesKt.getImageByFileName(name: self).toUIImage()!
    }
}

extension RateState {
    func getText() -> String {
        // swiftlint:disable force_cast
        switch self {
        case is RateState.Online:
            return MR.strings().text_online_last_updated.get(
                parameter: (self as! RateState.Online).lastUpdate ?? ""
            )
        case is RateState.Cached:
            return MR.strings().text_cached_last_updated.get(
                parameter: (self as! RateState.Cached).lastUpdate ?? ""
            )
        case is RateState.Offline:
            if let date = (self as! RateState.Offline).lastUpdate {
                return MR.strings().text_offline_last_updated.get(parameter: date)
            } else {
                return MR.strings().text_offline.get()
            }
        case is RateState.Error:
            return MR.strings().text_no_data.get()
        default:
            return ""
        }
    }

    func getColor() -> Color {
        switch self {
        case is RateState.Online:
            return MR.colors().online.get()
        case is RateState.Cached:
            return MR.colors().cached.get()
        case is RateState.Offline:
            return MR.colors().offline.get()
        case is RateState.Error:
            return MR.colors().error.get()
        default:
            return MR.colors().transparent.get()
        }
    }
}

extension String {
    func getSecretValue() -> String {
        return (Bundle.main.infoDictionary?[self] as? String) ?? "this is a secret value"
    }
}

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
