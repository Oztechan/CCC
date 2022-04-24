//
//  CalculationInputView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Resources

struct InputView: View {
    @Environment(\.colorScheme) var colorScheme

    var input: String
    var onSettingsClick: () -> Void

    var body: some View {
        HStack {

            Spacer()

            Text(input)
                .multilineTextAlignment(.center)
                .lineLimit(2)
                .fixedSize(horizontal: false, vertical: true)
                .foregroundColor(MR.colors().text.get())
                .font(.title2)
                .animation(.default)

            Spacer()

            ToolbarButton(
                clickEvent: onSettingsClick,
                imgName: "gear"
            ).padding(.trailing, 5)

        }
        .frame(width: .none, height: 36, alignment: .center)
        .padding(.top, 4)
    }
}
