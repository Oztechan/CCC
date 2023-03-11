//
//  CurrenciesItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Provider
import Res
import SwiftUI

struct CurrenciesItemView: View {
    @Environment(\.colorScheme) var colorScheme
    @State var item: Currency

    var onItemClick: () -> Void
    var onItemLongClick: () -> Void

    var body: some View {
        HStack {
            CurrencyImageView(imageName: item.code)

            Text(item.code)
                .font(relative: .footnote)
                .foregroundColor(Res.colors().text.get())

            Text(item.name)
                .font(relative: .footnote)
                .foregroundColor(Res.colors().text.get())

            Text(item.symbol)
                .font(relative: .footnote)
                .foregroundColor(Res.colors().text.get())

            Spacer()

            Image(systemName: item.isActive ? "checkmark.circle.fill" : "circle")
                .resize(widthAndHeight: 20.cp())
                .foregroundColor(Res.colors().secondary.get())
        }
        .contentShape(Rectangle())
        .padding(.vertical, 4.cp())
        .onTapGesture { onItemClick() }
        .onLongPressGesture { onItemLongClick() }
        .lineLimit(1)
    }
}
