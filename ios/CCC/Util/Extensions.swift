//
//  Extensions.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Client
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
        return Color(get())
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

extension RateState {
    func getText() -> String {
        // swiftlint:disable force_cast
        switch self {
        case is RateState.Online:
            return R.strings().text_online_last_updated.get(
                parameter: (self as! RateState.Online).lastUpdate ?? ""
            )
        case is RateState.Cached:
            return R.strings().text_cached_last_updated.get(
                parameter: (self as! RateState.Cached).lastUpdate ?? ""
            )
        case is RateState.Offline:
            if let date = (self as! RateState.Offline).lastUpdate {
                return R.strings().text_offline_last_updated.get(parameter: date)
            } else {
                return R.strings().text_offline.get()
            }
        case is RateState.Error:
            return R.strings().text_no_data.get()
        default:
            return ""
        }
    }

    func getColor() -> Color {
        switch self {
        case is RateState.Online:
            return R.colors().online.get()
        case is RateState.Cached:
            return R.colors().cached.get()
        case is RateState.Offline:
            return R.colors().offline.get()
        case is RateState.Error:
            return R.colors().error.get()
        default:
            return R.colors().transparent.get()
        }
    }
}

extension String {
    func getSecretValue() -> String {
        return (Bundle.main.infoDictionary?[self] as? String) ?? "this is a secret value"
    }
}
