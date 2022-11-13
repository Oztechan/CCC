//
//  CurrenciesItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res
import Provider

struct CurrenciesItemView: View {
    @Environment(\.colorScheme) var colorScheme
    @State var item: Currency

    var onItemClick: () -> Void
    var onItemLongClick: () -> Void

    var body: some View {
        HStack {

            CurrencyImageView(imageName: item.name)

            Text(item.name)
                .frame(width: 45)
                .foregroundColor(MR.colors().text.get())
            Text(item.longName)
                .font(relative: .footnote)
                .foregroundColor(MR.colors().text.get())
            Text(item.symbol)
                .font(relative: .footnote)
                .foregroundColor(MR.colors().text.get())
            Spacer()
            Image(systemName: item.isActive ? "checkmark.circle.fill" : "circle")
                .foregroundColor(MR.colors().accent.get())

        }
        .contentShape(Rectangle())
        .onTapGesture { onItemClick() }
        .onLongPressGesture { onItemLongClick() }
        .lineLimit(1)
    }
}
