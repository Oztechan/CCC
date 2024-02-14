//
//  BugReportSlideRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import NavigationStack
import Provider

struct BugReportSlideRootView: View {
    @Environment(\.colorScheme) private var colorScheme

    @EnvironmentObject private var navigationStack: NavigationStackCompat

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        SlideView(
            title: String(\.slide_bug_report_title),
            image: Image(systemName: "ant.fill"),
            subTitle1: String(\.slide_bug_report_text_1),
            subTitle2: String(\.slide_bug_report_text_2),
            buttonText: String(\.got_it),
            buttonAction: { navigationStack.push(CurrenciesRootView()) }
        )
        .onAppear {
            analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 2))
        }
    }
}
