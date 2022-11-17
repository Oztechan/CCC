//
//  SelectCurrencyItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res
import Provider

struct SelectCurrencyItemView: View {
    @Environment(\.colorScheme) var colorScheme
    var item: Currency

    var body: some View {
        HStack {

            CurrencyImageView(imageName: item.name)

            Text(item.name)
                .font(relative: .footnote)
                .foregroundColor(MR.colors().text.get())

            Text(item.longName)
                .font(relative: .footnote)
                .foregroundColor(MR.colors().text.get())

            Text(item.symbol)
                .font(relative: .footnote)
                .foregroundColor(MR.colors().text.get())

            Spacer()

        }
        .contentShape(Rectangle())
        .padding(.vertical, 4.cp())
        .lineLimit(1)
    }
}
