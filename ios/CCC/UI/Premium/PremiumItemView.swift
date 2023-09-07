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
    @Environment(\.colorScheme) private var colorScheme

    let item: PremiumType?

    var body: some View {
        HStack {
            if item == nil {
                Spacer()

                Text(Res.strings().txt_more_options_are_coming.get())
                    .font(relative: .footnote)
                    .foregroundColor(Res.colors().text.get())

                Spacer()
            } else {
                Text(item!.data.duration)
                    .font(relative: .callout)
                    .foregroundColor(Res.colors().text.get())

                Spacer()

                Text(item!.data.cost)
                    .font(relative: .callout)
                    .foregroundColor(Res.colors().text.get())
            }
        }
        .contentShape(Rectangle())
        .padding(4.cp())
        .lineLimit(1)
    }
}
