//
//  SelectCurrencyItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Provider
import SwiftUI

struct SelectCurrencyItemView: View {
    @Environment(\.colorScheme) private var colorScheme
    var item: Currency

    var body: some View {
        HStack {
            CurrencyImageView(imageName: item.code)

            Text(item.code)
                .font(relative: .footnote)
                .foregroundColor(\.text)

            Text(item.name)
                .font(relative: .footnote)
                .foregroundColor(\.text)

            Text(item.symbol)
                .font(relative: .footnote)
                .foregroundColor(\.text)

            Spacer()
        }
        .contentShape(Rectangle())
        .padding(.vertical, 4.cp())
        .lineLimit(1)
    }
}
