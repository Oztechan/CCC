//
//  PremiumRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Res
import Provider
import SwiftUI

struct PremiumRootView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        PremiumState,
        PremiumEffect,
        PremiumEvent,
        BaseData,
        PremiumViewModel
    >()
    @Environment(\.colorScheme) private var colorScheme
    @Binding var premiumViewVisibility: Bool
    @State var isPremiumDialogShown = false

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        PremiumView(
            event: observable.event,
            state: observable.state
        )
        .alert(isPresented: $isPremiumDialogShown) {
            AlertView(
                title: String(\.txt_premium),
                message: String(\.txt_premium_text),
                buttonText: String(\.txt_watch),
                buttonAction: {
                    RewardedAd(
                        onReward: {
                            observable.event.onPremiumActivated(
                                adType: PremiumType.video,
                                startDate: DateUtilKt.nowAsLong(),
                                isRestorePurchase: false
                            )
                        },
                        onError: {
                            observable.event.onPremiumActivationFailed()
                        }
                    ).show()
                }
            )
        }
        .onAppear {
            observable.startObserving()
            observable.event.onPremiumActivationFailed() // no billing implementation on iOS yet
            analyticsManager.trackScreen(screenName: ScreenName.Premium())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: PremiumEffect) {
        logger.i(message: { "PremiumRootView onEffect \(effect.description)" })
        switch effect {
        case let launchActivatePremiumFlowEffect as PremiumEffect.LaunchActivatePremiumFlow:
            if launchActivatePremiumFlowEffect.premiumType == PremiumType.video {
                isPremiumDialogShown.toggle()
            }
        default:
            logger.i(message: { "PremiumRootView unknown effect" })
        }
    }
}
