//
//  RateStateExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.11.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Provider
import Res
import SwiftUI

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
