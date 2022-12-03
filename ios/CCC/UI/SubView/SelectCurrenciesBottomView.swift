//
//  SelectCurrenciesBottomView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 29/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Res

struct SelectCurrenciesBottomView: View {
    @Environment(\.colorScheme) var colorScheme

    var text: String
    var buttonText: String
    var onButtonClick: () -> Void

    var body: some View {
        HStack {

            Text(text)
                .foregroundColor(MR.colors().text.get())
                .font(relative: .subheadline)

            Spacer()

            Button(
                action: onButtonClick,
                label: {
                    Text(buttonText)
                        .foregroundColor(MR.colors().text.get())
                        .font(relative: .footnote)
                }
            )
            .padding(top: 10.cp(), leading: 15.cp(), bottom: 10.cp(), trailing: 15.cp())
            .background(MR.colors().background_weak.get())
            .clipped()
            .cornerRadius(4.cp())

        }
        .padding(top: 5.cp(), leading: 15.cp(), bottom: 10.cp(), trailing: 10.cp())
    }
}
