//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 28/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import NavigationStack
import Client
import GoogleMobileAds

typealias MainObservable = ObservableSEED<MainViewModel, BaseState, MainEffect, MainEvent, MainData>

struct MainView: View {

    @StateObject var observable: MainObservable = koin.get()

    @Environment(\.scenePhase) var scenePhase

    var body: some View {

        NavigationStackView(
            transitionType: .default,
            easing: Animation.easeInOut(duration: 0.5)
        ) {
            if observable.viewModel.isFistRun() {
                SliderView()
            } else {
                CalculatorView()
            }
        }
        .onAppear {
            observable.startObserving()
            observable.event.onResume()
        }
        .onDisappear {
            observable.stopObserving()
            observable.event.onPause()
        }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: MainEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        case is MainEffect.ShowInterstitialAd:
            showInterstitialAd()
        default:
            LoggerKt.kermit.d(withMessage: {"MainView unknown effect"})
        }
    }

    private func showInterstitialAd() {
        GADInterstitialAd.load(
            withAdUnitID: "INTERSTITIAL_AD_ID".getSecretValue(),
            request: GADRequest(),
            completionHandler: { interstitialAd, error in
                if let error = error {
                    LoggerKt.kermit.d(withMessage: {"MainView showInterstitialAd \(error.localizedDescription)"})
                    return
                }

                interstitialAd?.present(
                    fromRootViewController: UIApplication.shared.windows.first!.rootViewController!
                )
            }
        )
    }
}
