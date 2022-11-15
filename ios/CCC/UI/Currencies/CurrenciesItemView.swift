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
                .font(relative: .footnote)
                .foregroundColor(MR.colors().text.get())

            Text(item.longName)
                .font(relative: .footnote)
                .foregroundColor(MR.colors().text.get())

            Text(item.symbol)
                .font(relative: .footnote)
                .foregroundColor(MR.colors().text.get())

            Spacer()

            Image(systemName: item.isActive ? "checkmark.circle.fill" : "circle")
                .resizable()
                .frame(width: 20.cp(), height: 20.cp(), alignment: .center)
                .foregroundColor(MR.colors().accent.get())

        }
        .contentShape(Rectangle())
        .padding(.vertical, 4.cp())
        .onTapGesture { onItemClick() }
        .onLongPressGesture { onItemLongClick() }
        .lineLimit(1)
    }
}
