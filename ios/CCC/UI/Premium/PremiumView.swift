//
//  PremiumView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Res
import Provider
import SwiftUI

struct PremiumView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        PremiumState,
        PremiumEffect,
        PremiumEvent,
        BaseData,
        PremiumViewModel
    >()
    @Environment(\.colorScheme) var colorScheme
    @Binding var premiumViewVisibility: Bool

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        NavigationView {
            ZStack {
                Color(Res.colors().background_strong.get()).edgesIgnoringSafeArea(.all)
                VStack {
                    Spacer()
                }.navigationBarHidden(true)
            }
        }
        .onAppear {
            observable.startObserving()
            analyticsManager.trackScreen(screenName: ScreenName.Premium())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: PremiumEffect) {
        logger.i(message: { "PremiumView onEffect \(effect.description)" })
    }
}
