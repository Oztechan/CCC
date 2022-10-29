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

            Text(item.rate)
                .foregroundColor(MR.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemAmountLongClick(item.rate) }

            Text(item.symbol)
                .foregroundColor(MR.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemAmountLongClick(item.rate) }

            Spacer()

            Text(item.name)
                .foregroundColor(MR.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemImageLongClick(item) }

            CurrencyImageView(imageName: item.name)
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemImageLongClick(item) }

        }
        .contentShape(Rectangle())
        .onTapGesture { onItemClick(item) }
    }
}
