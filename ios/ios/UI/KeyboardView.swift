//
//  KeyboardView.swift
//  ios
//
//  Created by Mustafa Ozhan on 24/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client

struct KeyboardView: View {
    var keyPressEvent: (String) -> Void

    let data = [
        [
            Strings.init().seven,
            Strings.init().eight,
            Strings.init().nine,
            Strings.init().multiply
        ],
        [
            Strings.init().four,
            Strings.init().five,
            Strings.init().six,
            Strings.init().divide
        ],
        [
            Strings.init().one,
            Strings.init().two,
            Strings.init().three,
            Strings.init().minus
        ],
        [
            Strings.init().dot,
            Strings.init().zero,
            Strings.init().percent,
            Strings.init().plus
        ],
        [
            Strings.init().openParentheses,
            Strings.init().tripleZero,
            Strings.init().ac,
            Strings.init().del,
            Strings.init().closeParentheses
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
                                    .foregroundColor(Color("ColorText"))
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
        }.background(Color("ColorBackgroundStrong"))
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
