//
//  IntroSlideRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import NavigationStack
import Provider

struct IntroSlideRootView: View {
    @Environment(\.colorScheme) private var colorScheme

    @EnvironmentObject private var navigationStack: NavigationStackCompat

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        SlideView(
            title: String(\.slide_intro_title),
            image: Image(resourceKey: \.ic_app_logo),
            subTitle1: String(\.slide_intro_text),
            subTitle2: "",
            buttonText: String(\.next),
            buttonAction: { navigationStack.push(PremiumSlideRootView()) }
        ).onAppear {
            analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 0))
        }
    }
}
