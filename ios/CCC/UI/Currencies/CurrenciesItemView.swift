//
//  CurrenciesItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Provider
import SwiftUI

struct CurrenciesItemView: View {
    @Environment(\.colorScheme) private var colorScheme
    @State var item: Currency

    var onItemClick: () -> Void
    var onItemLongClick: () -> Void

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

            Image(systemName: item.isActive ? "checkmark.circle.fill" : "circle")
                .resize(widthAndHeight: 20.cp())
                .foregroundColor(\.secondary)
        }
        .contentShape(Rectangle())
        .padding(.vertical, 4.cp())
        .onTapGesture { onItemClick() }
        .onLongPressGesture { onItemLongClick() }
        .lineLimit(1)
    }
}
