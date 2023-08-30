//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 28/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import NavigationStack
import Provider
import Res
import SwiftUI

struct MainRootView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        MainState,
        MainEffect,
        MainEvent,
        MainData,
        MainViewModel
    >()

    var body: some View {
        NavigationStackView(
            transitionType: .default,
            easing: Animation.easeInOut
        ) {
            MainView(state: observable.state)
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
        logger.i(message: { "MainRootView onEffect \(effect.description)" })
        switch effect {
        case is MainEffect.ShowInterstitialAd:
            InterstitialAd().show()
        default:
            logger.i(message: { "MainRootView unknown effect" })
        }
    }
}
