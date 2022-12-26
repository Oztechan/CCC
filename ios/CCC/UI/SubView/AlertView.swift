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
                .font(.headline)
                .padding(bottom: 20.cp())

            Text(message)
                .foregroundColor(MR.colors().text.get())
                .font(.subheadline)
                .multilineTextAlignment(.center)
                .padding(bottom: 30.cp())

            HStack {
                if isCancellable == true {
                    Button(
                        action: { },
                        label: {
                            Text(MR.strings().cancel.get())
                                .foregroundColor(MR.colors().background_weak.get())
                                .font(relative: .footnote)
                        }
                    )
                    .frame(width: 50.cp(), height: 15.cp(), alignment: .center)
                    .padding(10.cp())
                    .background(MR.colors().text.get())
                    .clipped()
                    .cornerRadius(4.cp())
                    .shadow(radius: 1)
                    .padding(.horizontal, 10.cp())
                }

                Button(
                    action: { buttonAction() },
                    label: {
                        Text(buttonText)
                            .foregroundColor(MR.colors().background_weak.get())
                            .font(relative: .footnote)
                    }
                )
                .frame(width: 50.cp(), height: 15.cp(), alignment: .center)
                .padding(10.cp())
                .background(MR.colors().primary.get())
                .clipped()
                .cornerRadius(4.cp())
                .shadow(radius: 1)
                .padding(.horizontal, 10.cp())
            }
        }
        .padding(20.cp())
        .background(MR.colors().background_weak.get())
        .cornerRadius(10.cp())
        .shadow(radius: 5)
        .padding(30.cp())
    }
}
