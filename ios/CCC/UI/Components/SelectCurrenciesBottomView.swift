//
//  SelectCurrenciesBottomView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 29/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Res
import SwiftUI

struct SelectCurrenciesBottomView: View {
    @Environment(\.colorScheme) var colorScheme

    var text: String
    var buttonText: String
    var onButtonClick: () -> Void

    var body: some View {
        HStack {
            Text(text)
                .foregroundColor(Res.colors().text.get())
                .font(relative: .subheadline)

            Spacer()

            ActionButton(
                buttonText: buttonText,
                buttonAction: onButtonClick,
                state: .neutral
            )
        }
        .padding(top: 5.cp(), leading: 15.cp(), bottom: 10.cp(), trailing: 10.cp())
    }
}
