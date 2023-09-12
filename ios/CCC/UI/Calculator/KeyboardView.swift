//
//  KeyboardView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct KeyboardView: View {
    var onKeyPress: (String) -> Void

    private let keys = [
        [String(\.seven), String(\.eight), String(\.nine), String(\.multiply)],
        [String(\.four), String(\.five), String(\.six), String(\.divide)],
        [String(\.one), String(\.two), String(\.three), String(\.minus)],
        [String(\.dot), String(\.zero), String(\.percent), String(\.plus)],
        // swiftlint:disable:next line_length
        [String(\.open_parentheses), String(\.triple_zero), String(\.ac), String(\.delete_), String(\.close_parentheses)]
    ]

    var body: some View {
        VStack(alignment: .center) {
            ForEach(keys, id: \.self) { items in
                HStack(alignment: .center) {
                    ForEach(items, id: \.self) { item in
                        Button(
                            action: { onKeyPress(item) },
                            label: {
                                Text(item)
                                    .font(relative: .title2)
                                    .foregroundColor(Color(\.text))
                                    .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity)
                            }
                        )
                    }
                }
            }
        }.background(Color(\.background_strong))
    }
}
