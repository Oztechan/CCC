//
//  CalculationOutputView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

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
                    .foregroundColor(\.text)
                    .font(relative: .body)

                if !output.isEmpty {
                    Text("=  \(output)")
                        .foregroundColor(\.text)
                        .font(relative: .body)
                        .onTapGesture {
                            onBarClick()
                        }
                        .onLongPressGesture {
                            onOutputLongClick()
                        }
                }

                Text(symbol)
                    .foregroundColor(\.text)
                    .font(relative: .body)
            }
            .frame(minWidth: 0, maxWidth: .infinity, alignment: .bottomLeading)
            .padding(.horizontal, 20.cp())
            .animation(.default, value: output)
        }
        .contentShape(Rectangle())
        .lineLimit(1)
        .onTapGesture { onBarClick() }
    }
}
