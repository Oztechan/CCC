//
//  FormProgressView.swift
//  ios
//
//  Created by Mustafa Ozhan on 27/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct FormProgressView: View {
    var body: some View {
        HStack {
            Spacer()
            ProgressView().transition(.slide)
            Spacer()
        }
        .listRowBackground(MR.colors().background.get())
    }
}
