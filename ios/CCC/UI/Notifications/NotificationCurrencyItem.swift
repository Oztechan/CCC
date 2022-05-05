//
//  NotificationCurrencyItem.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct NotificationCurrencyItem: View {
    let currencyName: String
    let clickAction: () -> Void

    var body: some View {
        HStack {
            Image(uiImage: currencyName.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)

            Text(currencyName)

        }.onTapGesture {
            clickAction()
        }
    }
}
