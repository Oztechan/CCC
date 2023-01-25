//
//  CalculatorItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Provider
import Res

struct CalculatorItemView: View {

    var item: Currency
    var onItemClick: (Currency) -> Void
    var onItemImageLongClick: (Currency) -> Void
    var onItemAmountLongClick: (String) -> Void

    var body: some View {
        HStack {

            Text(String(item.rate))
                .font(relative: .body)
                .foregroundColor(Res.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemAmountLongClick(String(item.rate)) }

            Text(item.symbol)
                .font(relative: .subheadline)
                .foregroundColor(Res.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemAmountLongClick(String(item.rate)) }

            Spacer()

            Text(item.code)
                .font(relative: .subheadline)
                .foregroundColor(Res.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemImageLongClick(item) }

            CurrencyImageView(imageName: item.code)
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemImageLongClick(item) }

        }
        .padding(.vertical, 4.cp())
        .contentShape(Rectangle())
        .onTapGesture { onItemClick(item) }
    }
}
