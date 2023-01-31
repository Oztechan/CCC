//
//  CalculationInputView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

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
                .foregroundColor(Res.colors().text.get())
                .font(relative: .title2)
                .animation(.none)

            Spacer()

            ToolbarButton(
                clickEvent: onSettingsClick,
                imgName: "gear"
            ).padding(.trailing, 5.cp())
        }
        .frame(width: .none, height: 36.cp(), alignment: .center)
        .padding(.top, 4.cp())
    }
}
