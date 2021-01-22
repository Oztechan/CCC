//
//  KeyboardView.swift
//  ios
//
//  Created by Mustafa Ozhan on 22/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct KeyboardView: View {
    var keyPressEvent: (String) -> Void

    let data = [
        [
            MR.strings().seven.get(),
            MR.strings().eight.get(),
            MR.strings().nine.get(),
            MR.strings().multiply.get()
        ],
        [
            MR.strings().four.get(),
            MR.strings().five.get(),
            MR.strings().six.get(),
            MR.strings().divide.get()
        ],
        [
            MR.strings().one.get(),
            MR.strings().two.get(),
            MR.strings().three.get(),
            MR.strings().minus.get()
        ],
        [
            MR.strings().dot.get(),
            MR.strings().zero.get(),
            MR.strings().percent.get(),
            MR.strings().plus.get()
        ],
        [
            MR.strings().open_parentheses.get(),
            MR.strings().triple_zero.get(),
            MR.strings().ac.get(),
            MR.strings().delete_.get(),
            MR.strings().close_parentheses.get()
        ]
    ]

    var body: some View {

        VStack(alignment: .center) {
            ForEach(data, id: \.self) { items in

                HStack(alignment: .center) {
                    ForEach(items, id: \.self) { item in

                        Button(
                            action: { keyPressEvent(item)},
                            label: {
                                Text(item)
                                    .font(.title2)
                                    .foregroundColor(MR.colors().text.get())
                                    .frame(
                                        minWidth: 0,
                                        maxWidth: .infinity,
                                        minHeight: 0,
                                        maxHeight: .infinity
                                    )
                            }
                        )

                    }
                }

            }
        }.background(MR.colors().background_strong.get())
    }
}

#if DEBUG
struct KeyboardViewPreviews: PreviewProvider {
    static var previews: some View {
        KeyboardView(keyPressEvent: {_ in })
            .previewLayout(.fixed(width: 300, height: 500))
            .makeForPreviewProvider()
    }
}
#endif
