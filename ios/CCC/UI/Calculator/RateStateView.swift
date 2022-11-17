//
//  RateStateView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct RateStateView: View {
    var color: Color
    var text: String

    var body: some View {
        HStack {
            Circle()
                .frame(width: 12.cp(), height: 12.cp(), alignment: .center)
                .foregroundColor(color)

            Text(text)
                .font(relative: .caption)
        }
        .padding(.bottom, 5.cp())
        .animation(.default)
    }
}
