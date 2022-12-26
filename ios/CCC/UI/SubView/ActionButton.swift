//
//  ActionButton.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct ActionButton: View {
    let buttonText: String
    let buttonAction: () -> Void
    let isPrimary: Bool

    var body: some View {
        Button(
            action: {
                buttonAction()
            },
            label: {
                Text(buttonText)
                    .foregroundColor(MR.colors().background_weak.get())
                    .font(relative: .footnote)
            }
        )
        .frame(width: 50.cp(), height: 15.cp(), alignment: .center)
        .padding(10.cp())
        .background(isPrimary ? MR.colors().primary.get() : MR.colors().text.get())
        .clipped()
        .cornerRadius(4.cp())
        .shadow(radius: 1)
        .padding(.horizontal, 10.cp())
    }
}
