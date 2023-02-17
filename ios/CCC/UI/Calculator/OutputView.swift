//
//  CalculationOutputView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

struct OutputView: View {
    var baseCurrency: String
    var output: String
    var symbol: String
    var onBarClick: () -> Void
    var onOutputLongClick: () -> Void

    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                CurrencyImageView(imageName: baseCurrency)

                Text(baseCurrency)
                    .foregroundColor(Res.colors().text.get())
                    .font(relative: .body)

                if !output.isEmpty {
                    Text("=  \(output)")
                        .foregroundColor(Res.colors().text.get())
                        .font(relative: .body)
                        .onLongPressGesture {
                            onOutputLongClick()
                        }
                }

                Text(symbol)
                    .foregroundColor(Res.colors().text.get())
                    .font(relative: .body)
            }
            .frame(minWidth: 0, maxWidth: .infinity, alignment: .bottomLeading)
            .padding(.horizontal, 20.cp())
            .animation(.default)
        }
        .contentShape(Rectangle())
        .lineLimit(1)
        .onTapGesture { onBarClick() }
    }
}
