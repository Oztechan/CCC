//
//  CalculationOutputView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Resources

struct OutputView: View {

    var baseCurrency: String
    var output: String
    var symbol: String
    var onBarClick: () -> Void

    var body: some View {
        VStack(alignment: .leading) {

            HStack {
                if baseCurrency != "" {
                    Image(uiImage: baseCurrency.getImage())
                        .resizable()
                        .frame(width: 36, height: 36, alignment: .center)
                        .shadow(radius: 3)
                }

                Text(baseCurrency).foregroundColor(MR.colors().text.get())

                if !output.isEmpty {
                    Text("=  \(output)").foregroundColor(MR.colors().text.get())
                }

                Text(symbol).foregroundColor(MR.colors().text.get())
            }
            .frame(minWidth: 0, maxWidth: .infinity, alignment: .bottomLeading)
            .padding(EdgeInsets(top: 0, leading: 20, bottom: 0, trailing: 20))
            .animation(.default)

        }
        .contentShape(Rectangle())
        .lineLimit(1)
        .onTapGesture { onBarClick() }
    }
}
