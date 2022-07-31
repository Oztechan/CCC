//
//  CalculatorItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Client
import Res

struct CalculatorItemView: View {

    var item: Currency
    var onItemClick: (Currency) -> Void
    var onItemImageLongClick: (Currency) -> Void
    var onItemAmountLongClick: (String) -> Void

    var body: some View {
        HStack {

            Text(IOSCalculatorUtilKt.getFormatted(item.rate, precision: Int32.init(3)))
                .foregroundColor(MR.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture {
                    onItemAmountLongClick(IOSCalculatorUtilKt.getFormatted(item.rate, precision: Int32.init(3)))
                }

            Text(item.symbol)
                .foregroundColor(MR.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture {
                    onItemAmountLongClick(IOSCalculatorUtilKt.getFormatted(item.rate, precision: Int32.init(3)))
                }

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
