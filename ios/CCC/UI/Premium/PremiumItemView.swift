//
//  PremiumItem.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Provider
import Res

struct PremiumItemView: View {
    @Environment(\.colorScheme) var colorScheme

    let item: PremiumType?

    var body: some View {
        HStack {
            Text(item!.data.duration)
                .font(relative: .callout)
                .foregroundColor(Res.colors().text.get())

            Spacer()

            Text(item!.data.cost)
                .font(relative: .callout)
                .foregroundColor(Res.colors().text.get())
        }
        .contentShape(Rectangle())
        .padding(4.cp())
        .lineLimit(1)
    }
}
