//
//  SwiftUIView.swift
//  ios
//
//  Created by Mustafa Ozhan on 21/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct CurrencyItemView: View {

    @Environment(\.colorScheme) var colorScheme

    @State var item: Currency

    var updateCurrencyEvent: () -> Void

    var body: some View {
        HStack {

            Image(uiImage: item.name.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)
            Text(item.name)
                .frame(width: 45)
                .foregroundColor(MR.colors().text.get())
            Text(item.longName)
                .font(.footnote)
                .foregroundColor(MR.colors().text.get())
            Text(item.symbol)
                .font(.footnote)
                .foregroundColor(MR.colors().text.get())
            Spacer()
            Image(systemName: item.isActive ? "checkmark.circle.fill" : "circle")

        }
        .contentShape(Rectangle())
        .onTapGesture { updateCurrencyEvent() }
        .lineLimit(1)
    }
}

//#if DEBUG
//struct SettingsItemViewPreviews: PreviewProvider {
//    static var previews: some View {
//        CurrencyItemView(item: Currency(), updateCurrencyEvent: {})
//            .previewLayout(.fixed(width: 300, height: 36))
//            .makeForPreviewProvider()
//    }
//}
//#endif
