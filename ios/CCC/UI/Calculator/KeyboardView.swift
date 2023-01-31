//
//  KeyboardView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

struct KeyboardView: View {
    var onKeyPress: (String) -> Void

    // swiftlint:disable line_length
    let keys = [
        [Res.strings().seven.get(), Res.strings().eight.get(), Res.strings().nine.get(), Res.strings().multiply.get()],
        [Res.strings().four.get(), Res.strings().five.get(), Res.strings().six.get(), Res.strings().divide.get()],
        [Res.strings().one.get(), Res.strings().two.get(), Res.strings().three.get(), Res.strings().minus.get()],
        [Res.strings().dot.get(), Res.strings().zero.get(), Res.strings().percent.get(), Res.strings().plus.get()],
        [
            Res.strings().open_parentheses.get(),
            Res.strings().triple_zero.get(),
            Res.strings().ac.get(),
            Res.strings().delete_.get(),
            Res.strings().close_parentheses.get()
        ]
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
                                    .foregroundColor(Res.colors().text.get())
                                    .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity)
                            }
                        )
                    }
                }
            }
        }.background(Res.colors().background_strong.get())
    }
}
