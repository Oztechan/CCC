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
    @Environment(\.colorScheme) var colorScheme: ColorScheme

    var text: String
    var iconName: String?
    var buttonText: String?
    var buttonAction: () -> Void = {}

    var body: some View {
        HStack {
            Image(uiImage: iconName?.getImage() ?? Res.images().ic_app_logo.get())
                .resize(widthAndHeight: 48.cp())

            Text(text)
                .foregroundColor(Res.colors().text.get())
                .font(relative: .subheadline)
                .padding(leading: 5.cp())

            Spacer()

            if buttonText != nil {
                ActionButton(
                    buttonText: buttonText!,
                    buttonAction: buttonAction,
                    state: .primary
                )
            }
        }
        .padding(10.cp())
        .background(Res.colors().background_weak.get())
        .cornerRadius(10.cp())
        .shadow(radius: 5)
        .padding(10.cp())
        .padding(bottom: DeviceUtil.getBottomNotchHeight().cp())
    }
}
