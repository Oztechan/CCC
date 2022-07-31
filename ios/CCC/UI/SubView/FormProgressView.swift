//
//  FormProgressView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Res

struct FormProgressView: View {
    @Environment(\.colorScheme) var colorScheme

    var body: some View {
        HStack {
            Spacer()
            ProgressView().transition(.slide)
            Spacer()
        }
        .listRowInsets(.init())
        .listRowBackground(MR.colors().background.get())
    }
}
