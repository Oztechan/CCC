//
//  SelectCurrencyItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Resources
import Client

struct SelectCurrencyItemView: View {
    @Environment(\.colorScheme) var colorScheme
    var item: Currency

    var body: some View {
        HStack {

            CurrencyImageView(imageName: item.name)

            Text(item.name)
                .frame(width: 45)
                .foregroundColor(MR.colors().text.get())
            Text(item.longName)
                .font(.footnote)
                .foregroundColor(MR.colors().text.get())
            Text(item.symbol)
                .font(.footnote)
                .foregroundColor(MR.colors().text.get())
            Spacer()

        }
        .contentShape(Rectangle())
        .lineLimit(1)
    }
}
