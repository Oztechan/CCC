//
//  KeyboardView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct KeyboardView: View {
    var onKeyPress: (String) -> Void

    // swiftlint:disable line_length
    let keys = [
        [MR.strings().seven.get(), MR.strings().eight.get(), MR.strings().nine.get(), MR.strings().multiply.get()],
        [MR.strings().four.get(), MR.strings().five.get(), MR.strings().six.get(), MR.strings().divide.get()],
        [MR.strings().one.get(), MR.strings().two.get(), MR.strings().three.get(), MR.strings().minus.get()],
        [MR.strings().dot.get(), MR.strings().zero.get(), MR.strings().percent.get(), MR.strings().plus.get()],
        [MR.strings().open_parentheses.get(), MR.strings().triple_zero.get(), MR.strings().ac.get(), MR.strings().delete_.get(), MR.strings().close_parentheses.get()]
    ]

    var body: some View {

        VStack(alignment: .center) {
            ForEach(keys, id: \.self) { items in

                HStack(alignment: .center) {
                    ForEach(items, id: \.self) { item in

                        Button(
                            action: { onKeyPress(item)},
                            label: {
                                Text(item)
                                    .font(.title2)
                                    .foregroundColor(MR.colors().text.get())
                                    .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity)
                            }
                        )

                    }
                }

            }
        }.background(MR.colors().background_strong.get())
    }
}
