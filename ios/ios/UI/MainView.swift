//
//  MainView.swift
//  ios
//
//  Created by Mustafa Ozhan on 28/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

typealias MainObservable = ObservableSEED<MainViewModel, BaseState, MainEffect, MainEvent, MainData>

struct MainView: View {

    @StateObject var seed: MainObservable = koin.get()

    @Environment(\.scenePhase) var scenePhase

    var body: some View {

        ZStack {
            if seed.viewModel.isFistRun() {
                CurrenciesView(currenciesNavigationToggle: .constant(false))
            } else {
                CalculatorView()
            }
        }
        .onAppear {seed.startObserving()}
        .onReceive(seed.effect) { onEffect(effect: $0) }
        .onChange(of: scenePhase) { phase in
            switch phase {
            case .background:
                LoggerKt.kermit.d(withMessage: {"App is in background"})
            case .active:
                LoggerKt.kermit.d(withMessage: {"App is Active"})
                seed.event.onResume()
            case .inactive:
                LoggerKt.kermit.d(withMessage: {"App is Inactive"})
                seed.event.onPause()
            @unknown default:
                LoggerKt.kermit.d(withMessage: {"New App state not yet introduced"})
            }
        }
    }

    private func onEffect(effect: MainEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}
