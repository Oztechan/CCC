//
//  SlideView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct SlideView: View {
    @Environment(\.colorScheme) var colorScheme

    var title: String
    var image: Image
    var subTitle1: String
    var subTitle2: String
    var buttonText: String
    var buttonAction: () -> Void

    var body: some View {
        ZStack {
            Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)
            VStack {

                Spacer()

                Text(title)
                    .multilineTextAlignment(.center)
                    .font(relative: .largeTitle)

                image
                    .frame(width: 196, height: 196, alignment: .center)
                    .font(size: 128)
                    .accentColor(MR.colors().text.get())

                Text(subTitle1)
                    .lineSpacing(12)
                    .multilineTextAlignment(.center)
                    .font(relative: .body)
                    .padding(10)
                    .padding(.bottom, 30)

                Text(subTitle2)
                    .lineSpacing(12)
                    .multilineTextAlignment(.center)
                    .padding(10)
                    .font(relative: .callout)

                Spacer()

                Divider()

                HStack {
                    Spacer()

                    Button(
                        action: buttonAction,
                        label: {
                            Text(buttonText)
                                .font(relative: .body)
                                .foregroundColor(MR.colors().text.get())
                        }
                    ).padding(top: 10, leading: 10, bottom: 15, trailing: 15)
                }
            }
        }
    }
}
