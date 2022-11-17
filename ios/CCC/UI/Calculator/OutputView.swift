//
//  CalculationOutputView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct OutputView: View {

    var baseCurrency: String
    var output: String
    var symbol: String
    var onBarClick: () -> Void

    var body: some View {
        VStack(alignment: .leading) {

            HStack {
                CurrencyImageView(imageName: baseCurrency)

                Text(baseCurrency)
                    .foregroundColor(MR.colors().text.get())
                    .font(relative: .callout)

                if !output.isEmpty {
                    Text("=  \(output)")
                        .foregroundColor(MR.colors().text.get())
                        .font(relative: .callout)
                }

                Text(symbol)
                    .foregroundColor(MR.colors().text.get())
                    .font(relative: .callout)
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
