//
//  CurrenciesView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 21/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import NavigationStack
import Provider
import Res
import SwiftUI

struct CurrenciesView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        CurrenciesState,
        CurrenciesEffect,
        CurrenciesEvent,
        CurrenciesData,
        CurrenciesViewModel
    >()
    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @State var isFewCurrencySnackShown = false

    private let analyticsManager: AnalyticsManager = koin.get()

    var onBaseChange: (String) -> Void

    var body: some View {
        ZStack {
            Res.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {
                if observable.state.selectionVisibility {
                    SelectionView(
                        onCloseClick: observable.event.onCloseClick,
                        updateAllCurrenciesState: { observable.event.updateAllCurrenciesState(state: $0) }
                    )
                } else {
                    CurrenciesToolbarView(
                        firstRun: observable.viewModel.isFirstRun(),
                        onBackClick: observable.event.onCloseClick,
                        onQueryChange: { observable.event.onQueryChange(query: $0) }
                    )
                }

                if observable.state.loading {
                    FormProgressView()
                } else {
                    Form {
                        List(observable.state.currencyList, id: \.code) { currency in
                            CurrenciesItemView(
                                item: currency,
                                onItemClick: { observable.event.onItemClick(currency: currency) },
                                onItemLongClick: observable.event.onItemLongClick
                            )
                        }
                        .listRowInsets(.init())
                        .id(UUID())
                        .listRowBackground(Res.colors().background.get())
                    }
                    .withClearBackground(color: Res.colors().background.get())
                }

                if observable.viewModel.isFirstRun() {
                    SelectCurrenciesBottomView(
                        text: Res.strings().txt_select_currencies.get(),
                        buttonText: Res.strings().btn_done.get(),
                        onButtonClick: observable.event.onDoneClick
                    )
                }

                if observable.viewModel.shouldShowBannerAd() {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_CURRENCIES").adapt()
                }
            }
            .animation(.default)
            .navigationBarHidden(true)
        }
        .popup(
            isPresented: $isFewCurrencySnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().choose_at_least_two_currency.get())
        }
        .onAppear {
            observable.startObserving()
            analyticsManager.trackScreen(screenName: ScreenName.Currencies())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: CurrenciesEffect) {
        logger.i(message: { "CurrenciesView onEffect \(effect.description)" })
        switch effect {
        case is CurrenciesEffect.FewCurrency:
            isFewCurrencySnackShown.toggle()
        case is CurrenciesEffect.OpenCalculator:
            navigationStack.push(CalculatorView())
        case is CurrenciesEffect.Back:
            navigationStack.pop()
        // swiftlint:disable force_cast
        case is CurrenciesEffect.ChangeBase:
            onBaseChange((effect as! CurrenciesEffect.ChangeBase).newBase)
        default:
            logger.i(message: { "CurrenciesView unknown effect" })
        }
    }
}
