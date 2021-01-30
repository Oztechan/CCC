//
//  SliderView.swift
//  ios
//
//  Created by Mustafa Ozhan on 30/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client
import NavigationStack

struct SliderView: View {
    @EnvironmentObject private var navigationStack: NavigationStack
    @Environment(\.colorScheme) var colorScheme

    var body: some View {

        VStack {

            SlideView(
                title: MR.strings().slide_intro_title.get(),
                image: Image(uiImage: MR.images().ic_app_logo.get()),
                subTitle1: MR.strings().slide_intro_text.get(),
                subTitle2: "",
                butonText: MR.strings().next.get(),
                butonAction: {
                    navigationStack.push(

                        SlideView(
                            title: MR.strings().slide_bug_report_title.get(),
                            image: Image(systemName: "ant.fill"),
                            subTitle1: MR.strings().slide_bug_report_text_1.get(),
                            subTitle2: MR.strings().slide_bug_report_text_2.get(),
                            butonText: MR.strings().next.get(),
                            butonAction: {
                                navigationStack.push(

                                    SlideView(
                                        title: MR.strings().slide_disable_ads_title.get(),
                                        image: Image(systemName: "eye.slash.fill"),
                                        subTitle1: MR.strings().slide_disable_ads_text_1.get(),
                                        subTitle2: MR.strings().slide_disable_ads_text_2.get(),
                                        butonText: MR.strings().next.get(),
                                        butonAction: {
                                            navigationStack.push(

                                                SlideView(
                                                    title: MR.strings().slide_dark_mode_title.get(),
                                                    image: Image(systemName: "lightbulb.slash"),
                                                    subTitle1: MR.strings().slide_dark_mode_text_1.get(),
                                                    subTitle2: MR.strings().slide_dark_mode_text_2.get(),
                                                    butonText: MR.strings().btn_done.get(),
                                                    butonAction: { navigationStack.push(CurrenciesView()) }
                                                )

                                            )
                                        }
                                    )

                                )
                            }
                        )

                    )
                }
            )
        }
    }
}

struct SlideView: View {
    var title: String
    var image: Image
    var subTitle1: String
    var subTitle2: String
    var butonText: String
    var butonAction: () -> Void

    var body: some View {
        ZStack {
            Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)
            VStack {

                Spacer()

                Text(title)
                    .multilineTextAlignment(.center)
                    .font(.largeTitle)

                image
                    .frame(width: 225, height: 225, alignment: .center)
                    .font(.system(size: 128))
                    .imageScale(.large)
                    .accentColor(MR.colors().text.get())

                Text(subTitle1)
                    .multilineTextAlignment(.center)
                    .font(.body)
                    .padding(10)
                    .padding(.bottom, 30)

                Text(subTitle2)
                    .multilineTextAlignment(.center)
                    .padding(10)
                    .font(.callout)

                Spacer()

                Divider()

                HStack {
                    Spacer()

                    Button(action: { butonAction() },
                           label: {
                            Text(butonText)
                                .font(.body)
                                .foregroundColor(MR.colors().text.get())
                           }
                    ).padding(EdgeInsets(top: 10, leading: 10, bottom: 5, trailing: 15))
                }
            }
        }
    }
}

struct SliderViewPreviews: PreviewProvider {
    static var previews: some View {
        SliderView().colorScheme(.light)
    }
}
