//
//  ConversionStateExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.11.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Provider
import Res
import SwiftUI

extension ConversionState {
    func getText() -> String {
        // swiftlint:disable force_cast
        switch self {
        case is ConversionState.Online:
            return MR.strings().text_online_last_updated.get(
                parameter: (self as! ConversionState.Online).lastUpdate ?? ""
            )
        case is ConversionState.Cached:
            return MR.strings().text_cached_last_updated.get(
                parameter: (self as! ConversionState.Cached).lastUpdate ?? ""
            )
        case is ConversionState.Offline:
            if let date = (self as! ConversionState.Offline).lastUpdate {
                return MR.strings().text_offline_last_updated.get(parameter: date)
            } else {
                return MR.strings().text_offline.get()
            }
        case is ConversionState.Error:
            return MR.strings().text_no_data.get()
        default:
            return ""
        }
    }

    func getColor() -> Color {
        switch self {
        case is ConversionState.Online:
            return MR.colors().success.get()
        case is ConversionState.Cached:
            return MR.colors().info.get()
        case is ConversionState.Offline:
            return MR.colors().warning.get()
        case is ConversionState.Error:
            return MR.colors().error.get()
        default:
            return MR.colors().transparent.get()
        }
    }
}
