//
//  CalculatorItemView.swift
//  ios
//
//  Created by Mustafa Ozhan on 22/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct CalculatorItemView: View {

    var item: Currency
    var itemClickEvent: (Currency) -> Void

    var body: some View {
        HStack {

            Text(String(item.rate)).foregroundColor(Color(MR.colors().text.get()))
            Text(item.symbol).foregroundColor(Color(MR.colors().text.get()))
            Spacer()
            Text(item.name).foregroundColor(Color(MR.colors().text.get()))
            Image(uiImage: item.name.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)

        }
        .contentShape(Rectangle())
        .onTapGesture { itemClickEvent(item) }
    }
}

#if DEBUG
struct CalculatorItemViewPreviews: PreviewProvider {
    static var previews: some View {
        CalculatorItemView(item: Currency(), itemClickEvent: {_ in})
            .previewLayout(.fixed(width: 300, height: 60))
            .makeForPreviewProvider()
    }
}
#endif
