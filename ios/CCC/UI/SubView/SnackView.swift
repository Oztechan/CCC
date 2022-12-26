//
//  SnackView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct SnackView: View {
    var text: String
    var iconName: String?
    var buttonText: String?
    var buttonAction: () -> Void = {}

    var body: some View {
        HStack {
            Image(uiImage: iconName?.getImage() ?? MR.images().ic_app_logo.get())
                .resize(widthAndHeight: 64.cp())

            Text(text)
                .foregroundColor(MR.colors().text.get())
                .font(relative: .subheadline)
                .padding(leading: 5.cp())

            Spacer()

            if buttonText != nil {
                ActionButton(
                    buttonText: buttonText!,
                    buttonAction: buttonAction,
                    isPrimary: true
                )
            }
        }
        .padding(10.cp())
        .background(MR.colors().background_weak.get())
        .cornerRadius(10.cp())
        .shadow(radius: 5)
        .padding(10.cp())
    }
}
