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
        VStack {
            HStack {
                Spacer()

                ProgressView()
                    .progressViewStyle(.circular)
                    .transition(.slide)
                    .scaleEffect(1.25.cp())

                Spacer()
            }
            Spacer()
        }
        .padding(.top, 20.cp())
        .background(MR.colors().background.get())
    }
}
