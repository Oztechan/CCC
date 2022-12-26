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

    let title: String
    let message: String
    let buttonText: String
    var buttonAction: (() -> Void) = { }
    var isCancellable: Bool = true

    var body: some View {

        VStack {

            Text(title)
                .foregroundColor(MR.colors().text.get())
                .font(relative: .headline)
                .padding(bottom: 20.cp())

            Text(message)
                .foregroundColor(MR.colors().text.get())
                .font(relative: .subheadline)
                .multilineTextAlignment(.center)
                .padding(bottom: 30.cp())

            HStack {
                if isCancellable == true {
                    ActionButton(
                        buttonText: MR.strings().cancel.get(),
                        buttonAction: {},
                        isPrimary: false
                    )
                }

                ActionButton(
                    buttonText: buttonText,
                    buttonAction: buttonAction,
                    isPrimary: true
                )
            }
        }
        .padding(20.cp())
        .background(MR.colors().background_weak.get())
        .cornerRadius(10.cp())
        .shadow(radius: 5)
        .padding(30.cp())
    }
}
