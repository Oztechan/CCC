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
//            Color(Res.colors().background_strong.get()).edgesIgnoringSafeArea(.all)
            VStack {

                Spacer()

                Text(title)
                    .multilineTextAlignment(.center)
                    .font(relative: .largeTitle)

                image
                    .resize(widthAndHeight: 144.cp())
//                    .accentColor(Res.colors().text.get())
                    .padding(4.cp())

                Text(subTitle1)
                    .lineSpacing(12.cp())
                    .multilineTextAlignment(.center)
                    .font(relative: .body)
                    .padding(10.cp())
                    .padding(.bottom, 30.cp())

                Text(subTitle2)
                    .lineSpacing(12.cp())
                    .multilineTextAlignment(.center)
                    .padding(10.cp())
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
//                                .foregroundColor(Res.colors().text.get())
                        }
                    ).padding(top: 10.cp(), leading: 10.cp(), bottom: 15.cp(), trailing: 15.cp())
                }
            }
        }
    }
}

struct SlideViewPreviews: PreviewProvider {
    static var previews: some View {
        SlideView(
//            title: MR.strings().slide_intro_title.get() // this doens't work too
            title: "Test Title",
            image: Image(systemName: "gear"),
            subTitle1: "Subtitle 1",
            subTitle2: "Subtitle 2",
            buttonText: "Button",
            buttonAction: {}
        )
    }
}
