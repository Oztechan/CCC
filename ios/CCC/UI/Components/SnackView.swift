//
//  SnackView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct SnackView: View {
    @Environment(\.colorScheme) private var colorScheme: ColorScheme

    var text: String
    var iconName: String?
    var buttonText: String?
    var buttonAction: () -> Void = {}

    var body: some View {
        HStack {
            if iconName == nil {
                Image(resourceKey: \.ic_app_logo).resize(widthAndHeight: 48.cp())
            } else {
                Image(imageName: iconName!).resize(widthAndHeight: 48.cp())
            }

            Text(text)
                .foregroundColor(Color(\.text))
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
        .background(Color(\.background_weak))
        .cornerRadius(10.cp())
        .shadow(radius: 5)
        .padding(10.cp())
        .padding(bottom: DeviceUtil.getBottomNotchHeight().cp())
    }
}
