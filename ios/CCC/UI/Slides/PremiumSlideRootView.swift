//
//  PremiumSlideRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Res
import SwiftUI
import NavigationStack
import Provider

struct PremiumSlideRootView: View {
    @Environment(\.colorScheme) private var colorScheme

    @EnvironmentObject private var navigationStack: NavigationStackCompat

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        SlideView(
            title: Res.strings().slide_premium_title.get(),
            image: Image(systemName: "crown.fill"),
            subTitle1: Res.strings().slide_premium_text_1_no_ads.get(),
            subTitle2: Res.strings().slide_premium_text_2.get(),
            buttonText: Res.strings().next.get(),
            buttonAction: { navigationStack.push(BugReportSlideRootView()) }
        ).onAppear {
            analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 1))
        }
    }
}
