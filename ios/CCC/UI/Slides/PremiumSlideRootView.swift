//
//  PremiumSlideRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import NavigationStack
import Provider

struct PremiumSlideRootView: View {
    @Environment(\.colorScheme) private var colorScheme

    @EnvironmentObject private var navigationStack: NavigationStackCompat

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        SlideView(
            title: String(\.slide_premium_title),
            image: Image(systemName: "crown.fill"),
            subTitle1: String(\.slide_premium_text_1_no_ads),
            subTitle2: String(\.slide_premium_text_2),
            buttonText: String(\.next),
            buttonAction: { navigationStack.push(BugReportSlideRootView()) }
        ).onAppear {
            analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 1))
        }
    }
}
