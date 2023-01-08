//
//  AlertView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct AlertView: View {

    @Environment(\.colorScheme) var colorScheme: ColorScheme
    let title: String
    let message: String
    let buttonText: String
    var buttonAction: (() -> Void) = { }
    var isCancellable: Bool = true

    var body: some View {

        VStack {

            Text(title)
                .foregroundColor(Res.colors().text.get())
                .font(relative: .headline)
                .padding(bottom: 20.cp())

            Text(message)
                .foregroundColor(Res.colors().text.get())
                .font(relative: .subheadline)
                .multilineTextAlignment(.center)
                .padding(bottom: 30.cp())

            HStack {
                if isCancellable == true {
                    ActionButton(
                        buttonText: Res.strings().cancel.get(),
                        buttonAction: {},
                        state: .secondary
                    )
                }

                ActionButton(
                    buttonText: buttonText,
                    buttonAction: buttonAction,
                    state: .primary
                )
            }
        }
        .padding(20.cp())
        .background(Res.colors().background_weak.get())
        .cornerRadius(10.cp())
        .shadow(radius: 5)
        .padding(30.cp())
    }
}
