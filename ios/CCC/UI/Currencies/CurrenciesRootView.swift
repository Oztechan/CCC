//
//  CurrenciesRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 21/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import NavigationStack
import Provider
import SwiftUI

struct CurrenciesRootView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        CurrenciesState,
        CurrenciesEffect,
        CurrenciesEvent,
        CurrenciesData,
        CurrenciesViewModel
    >()
    @Environment(\.colorScheme) private var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @State var isFewCurrencySnackShown = false

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        CurrenciesView(
            event: observable.event,
            state: observable.state
        ).snack(isPresented: $isFewCurrencySnackShown) {
            SnackView(text: String(\.choose_at_least_two_currency))
        }
        .onAppear {
            observable.startObserving()
            analyticsManager.trackScreen(screenName: ScreenName.Currencies())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: CurrenciesEffect) {
        logger.i(message: { "CurrenciesRootView onEffect \(effect.description)" })
        switch effect {
        case is CurrenciesEffect.FewCurrency:
            isFewCurrencySnackShown.toggle()
        case is CurrenciesEffect.OpenCalculator:
            navigationStack.push(CalculatorRootView())
        case is CurrenciesEffect.Back:
            navigationStack.pop()
        default:
            logger.i(message: { "CurrenciesRootView unknown effect" })
        }
    }
}
