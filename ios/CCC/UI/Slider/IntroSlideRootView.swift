//
//  IntroSlideRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Res
import SwiftUI
import NavigationStack
import Provider

struct IntroSlideRootView: View {
    @Environment(\.colorScheme) private var colorScheme

    @EnvironmentObject private var navigationStack: NavigationStackCompat

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        SlideView(
            title: Res.strings().slide_intro_title.get(),
            image: Image(uiImage: Res.images().ic_app_logo.get()),
            subTitle1: Res.strings().slide_intro_text.get(),
            subTitle2: "",
            buttonText: Res.strings().next.get(),
            buttonAction: { navigationStack.push(PremiumRootView()) }
        ).onAppear {
            analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 0))
        }
    }
}
