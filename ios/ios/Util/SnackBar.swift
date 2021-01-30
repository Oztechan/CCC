//
//  SnackBar.swift
//  ios
//
//  Created by Mustafa Ozhan on 30/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct Snackbar: View {
    @Environment(\.colorScheme) var colorScheme

    @Binding var isShowing: Bool
    private let presenting: AnyView
    private let text: Text
    private let actionText: Text?
    private let action: (() -> Void)?

    private var isBeingDismissedByAction: Bool {
        actionText != nil && action != nil
    }

    init<Presenting>(
        isShowing: Binding<Bool>,
        presenting: Presenting,
        text: Text,
        actionText: Text? = nil,
        action: (() -> Void)? = nil
    ) where Presenting: View {

        self._isShowing = isShowing
        self.presenting = AnyView(presenting)
        self.text = text
        self.actionText = actionText
        self.action = action

    }

    var body: some View {

            self.presenting
            VStack {
                Spacer()
                if self.isShowing {
                    HStack {
                        Image(uiImage: MR.images().ic_app_logo.get())
                            .resizable()
                            .frame(width: 64, height: 64)
                        self.text
                            .foregroundColor(MR.colors().text.get())
                        Spacer()
                        if self.actionText != nil && self.action != nil {
                            self.actionText!
                                .bold()
                                .padding(10)
                                .foregroundColor(MR.colors().text.get())
                                .background(MR.colors().background.get())
                                .clipped()
                                .cornerRadius(3)
                                .onTapGesture {
                                    self.action?()
                                    withAnimation {
                                        self.isShowing = false
                                    }
                                }
                        }
                    }
                    .padding()
                    .cornerRadius(5)
                    .shadow(radius: 3)
                    .background(MR.colors().background_weak.get())
                    .transition(
                        .asymmetric(
                            insertion: .move(edge: .bottom),
                            removal: .move(edge: .bottom)
                        )
                    )
                    .animation(.easeInOut(duration: 0.5))
                    .onAppear {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                            withAnimation {
                                self.isShowing = false
                            }
                        }
                    }
                }
        }
    }

}

extension View {

    func snackBar(isShowing: Binding<Bool>,
                  text: Text,
                  actionText: Text? = nil,
                  action: (() -> Void)? = nil) -> some View {

        Snackbar(isShowing: isShowing,
                 presenting: self,
                 text: text,
                 actionText: actionText,
                 action: action)

    }

}
