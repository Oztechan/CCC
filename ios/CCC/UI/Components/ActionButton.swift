//
//  ActionButton.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.12.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ActionButton: View {
    @Environment(\.colorScheme) private var colorScheme: ColorScheme

    let buttonText: String
    let buttonAction: () -> Void
    let state: State

    enum State {
        case primary, secondary, neutral
    }

    var body: some View {
        Button(
            action: {
                buttonAction()
            },
            label: {
                Text(buttonText)
                    .foregroundColor(getTextColor())
                    .font(relative: .footnote)
            }
        )
        .frame(width: 50.cp(), height: 15.cp(), alignment: .center)
        .padding(10.cp())
        .background(getBackgroundColor())
        .clipped()
        .cornerRadius(4.cp())
        .shadow(radius: 1)
        .padding(.horizontal, 10.cp())
    }

    private func getBackgroundColor() -> Color {
        switch state {
        case .primary:
            return Color(\.primary)
        case .secondary:
            return Color(\.text)
        case .neutral:
            return Color(\.background_weak)
        }
    }

    private func getTextColor() -> Color {
        switch state {
        case .primary, .secondary:
            return Color(\.background_weak)
        case .neutral:
            return Color(\.text)
        }
    }
}
