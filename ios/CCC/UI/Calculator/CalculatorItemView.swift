//
//  CalculatorItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Client
import Resources

struct CalculatorItemView: View {

    var item: Currency
    var onItemClick: (Currency) -> Void
    var onItemImageLongClick: (Currency) -> Void
    var onItemAmountLongClick: (String) -> Void

    var body: some View {
        HStack {

            Text(IOSCalculatorUtilKt.getFormatted(item.rate))
                .foregroundColor(MR.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemAmountLongClick(IOSCalculatorUtilKt.getFormatted(item.rate)) }

            Text(item.symbol)
                .foregroundColor(MR.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemAmountLongClick(IOSCalculatorUtilKt.getFormatted(item.rate)) }

            Spacer()

            Text(item.name)
                .foregroundColor(MR.colors().text.get())
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemImageLongClick(item) }

            Image(uiImage: item.name.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)
                .onTapGesture { onItemClick(item) }
                .onLongPressGesture { onItemImageLongClick(item) }

        }
        .contentShape(Rectangle())
        .onTapGesture { onItemClick(item) }
    }
}
