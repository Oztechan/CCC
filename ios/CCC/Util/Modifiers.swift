//
//  Modifiers.swift
//  CCC
//
//  Created by Mustafa Ozhan on 25.09.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct FormWithClearBackground: ViewModifier {
    let color: Color

    @ViewBuilder
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content
                .background(color)
                .scrollContentBackground(.hidden)
        } else {
            content
                .background(color)
        }
    }
}
