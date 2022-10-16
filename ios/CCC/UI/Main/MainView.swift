//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 28/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import NavigationStack
import Res
import Provider
import GoogleMobileAds

struct MainView: View {

    @StateObject var observable = ObservableSEEDViewModel<
        BaseState,
        MainEffect,
        MainEvent,
        MainData,
        MainViewModel
    >()

    var body: some View {

        NavigationStackView(
            transitionType: .custom(.opacity),
            easing: .default
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
        logger.i(message: {"MainView onEffect \(effect.description)"})
        switch effect {
        case is MainEffect.ShowInterstitialAd:
            InterstitialAd().show()
        default:
            logger.i(message: {"MainView unknown effect"})
        }
    }
}
