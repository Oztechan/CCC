//
//  CalculationOutputView.swift
//  ios
//
//  Created by Mustafa Ozhan on 22/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct CalculationOutputView: View {

    var baseCurrency: String
    var output: String
    var symbol: String
    var onBarClick: () -> Void

    var body: some View {
        VStack(alignment: .leading) {

            HStack {
                Image(uiImage: baseCurrency.getImage())
                    .resizable()
                    .frame(width: 36, height: 36, alignment: .center)
                    .shadow(radius: 3)
                Text(baseCurrency).foregroundColor(MR.colors().text.get())

                if !output.isEmpty {
                    Text(output).foregroundColor(MR.colors().text.get())
                }

                Text(symbol).foregroundColor(MR.colors().text.get())
            }
            .frame(minWidth: 0, maxWidth: .infinity, alignment: .bottomLeading)
            .padding(EdgeInsets(top: 0, leading: 20, bottom: 0, trailing: 20))

        }
        .contentShape(Rectangle())
        .lineLimit(1)
        .onTapGesture { onBarClick() }
    }
}

#if DEBUG
struct CalculationOutputViewPreview: PreviewProvider {
    static var previews: some View {
        CalculationOutputView(
            baseCurrency: "USD",
            output: "123",
            symbol: "$",
            onBarClick: {}
        ).makeForPreviewProvider()
    }
}
#endif
