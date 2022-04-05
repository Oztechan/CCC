//
//  CurrencySelectionView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 29/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Client

struct SelectCurrencyView: View {
    @Environment(\.colorScheme) var colorScheme

    var text: String
    var buttonText: String
    var onButtonClick: () -> Void

    var body: some View {
        HStack {

            Text(text)
                .foregroundColor(MR.colors().text.get())
                .font(.subheadline)
            Spacer()
            Button(
                action: onButtonClick,
                label: { Text(buttonText).foregroundColor(MR.colors().text.get()) }
            )
            .padding(EdgeInsets(top: 10, leading: 15, bottom: 10, trailing: 15))
            .background(MR.colors().background_weak.get())
            .clipped()
            .cornerRadius(4)

        }
        .padding(EdgeInsets(top: 5, leading: 15, bottom: 10, trailing: 10))
    }
}
