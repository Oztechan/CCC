//
//  SliderView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 30/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//
import SwiftUI
import Res
import NavigationStack
import Provider

struct SliderView: View {
    @EnvironmentObject private var navigationStack: NavigationStackCompat

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {

        VStack {

            SlideView(
                title: Res.strings().slide_intro_title.get(),
                image: Image(uiImage: Res.images().ic_app_logo.get()),
                subTitle1: Res.strings().slide_intro_text.get(),
                subTitle2: "",
                buttonText: Res.strings().next.get(),
                buttonAction: {
                    navigationStack.push(

                        SlideView(
                            title: Res.strings().slide_premium_title.get(),
                            image: Image(systemName: "crown.fill"),
                            subTitle1: Res.strings().slide_premium_text_1_no_ads.get(),
                            subTitle2: Res.strings().slide_premium_text_2.get(),
                            buttonText: Res.strings().next.get(),
                            buttonAction: {
                                navigationStack.push(

                                    SlideView(
                                        title: Res.strings().slide_bug_report_title.get(),
                                        image: Image(systemName: "ant.fill"),
                                        subTitle1: Res.strings().slide_bug_report_text_1.get(),
                                        subTitle2: Res.strings().slide_bug_report_text_2.get(),
                                        buttonText: Res.strings().got_it.get(),
                                        buttonAction: {
                                            navigationStack.push(
                                                CurrenciesView(onBaseChange: { _ in })
                                            )
                                        }
                                    ).onAppear {
                                        analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 2))
                                    }

                                )
                            }
                        ).onAppear {
                            analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 1))
                        }

                    )
                }
            ).onAppear {
                analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 0))
            }
        }
    }
}
