//
//  BugReportSlideRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Res
import SwiftUI
import NavigationStack
import Provider

struct BugReportSlideRootView: View {
    @Environment(\.colorScheme) private var colorScheme

    @EnvironmentObject private var navigationStack: NavigationStackCompat

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        SlideView(
            title: Res.strings().slide_bug_report_title.get(),
            image: Image(systemName: "ant.fill"),
            subTitle1: Res.strings().slide_bug_report_text_1.get(),
            subTitle2: Res.strings().slide_bug_report_text_2.get(),
            buttonText: Res.strings().got_it.get(),
            buttonAction: { navigationStack.push(CurrenciesRootView(onBaseChange: { _ in })) }
        )
        .onAppear {
            analyticsManager.trackScreen(screenName: ScreenName.Slider(position: 2))
        }
    }
}
