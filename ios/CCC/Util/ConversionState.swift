//
//  ConversionStateExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.11.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Provider
import SwiftUI

extension ConversionState {
    func getText() -> String {
        switch self {
        case let onlineState as ConversionState.Online:
            return String(\.text_online_last_updated, parameter: onlineState.lastUpdate ?? "")
        case let cachedState as ConversionState.Cached:
            return String(\.text_cached_last_updated, parameter: cachedState.lastUpdate ?? "")
        case let offlineState as ConversionState.Offline:
            if let date = offlineState.lastUpdate {
                return String(\.text_offline_last_updated, parameter: date)
            } else {
                return String(\.text_offline)
            }
        case is ConversionState.Error:
            return String(\.text_no_data)
        default:
            return ""
        }
    }

    func getColor() -> Color {
        switch self {
        case is ConversionState.Online:
            return Color(\.success)
        case is ConversionState.Cached:
            return Color(\.info)
        case is ConversionState.Offline:
            return Color(\.warning)
        case is ConversionState.Error:
            return Color(\.error)
        default:
            return Color(\.transparent)
        }
    }
}
