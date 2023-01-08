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
    @Environment(\.colorScheme) var colorScheme: ColorScheme

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
            return Res.colors().primary.get()
        case .secondary:
            return Res.colors().text.get()
        case .neutral:
            return Res.colors().background_weak.get()
        }
    }

    private func getTextColor() -> Color {
        switch state {
        case .primary, .secondary:
            return Res.colors().background_weak.get()
        case .neutral:
            return Res.colors().text.get()
        }
    }
}
