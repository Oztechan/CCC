//
//  SelectCurrencyObservable.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Res
import Provider
import NavigationStack

struct SelectCurrencyView: View {

    @StateObject var observable = ObservableSEEDViewModel<
        SelectCurrencyState,
        SelectCurrencyEffect,
        SelectCurrencyEvent,
        BaseData,
        SelectCurrencyViewModel
    >()
    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @Binding var isBarShown: Bool

    private let analyticsManager: AnalyticsManager = koin.get()

    var onCurrencySelected: (String) -> Void

    var body: some View {

        NavigationView {

            ZStack {

                Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                VStack {
                    Form {
                        if observable.state.loading {
                            FormProgressView()
                        } else {

                            List(observable.state.currencyList, id: \.name) { currency in

                                SelectCurrencyItemView(item: currency)
                                    .onTapGesture { observable.event.onItemClick(currency: currency) }
                                    .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)

                            }.listRowInsets(.init())
                            .listRowBackground(MR.colors().background.get())
                        }
                    }
                    .background(MR.colors().background.get())
                    .navigationBarTitle(MR.strings().txt_select_base_currency.get())

                    Spacer()

                    SelectCurrenciesBottomView(
                        text: observable.state.enoughCurrency ?
                        MR.strings().txt_update_favorite_currencies.get() :
                            MR.strings().choose_at_least_two_currency.get(),
                        buttonText: observable.state.enoughCurrency ?
                        MR.strings().update.get() :
                            MR.strings().select.get(),
                        onButtonClick: observable.event.onSelectClick
                    ).listRowBackground(MR.colors().background.get())
                }
            }
        }
        .onAppear {
            observable.startObserving()
            analyticsManager.trackScreen(screenName: ScreenName.SelectCurrency())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: SelectCurrencyEffect) {
        logger.i(message: {"SelectCurrencyView onEffect \(effect.description)"})
        switch effect {
        // swiftlint:disable force_cast
        case is SelectCurrencyEffect.CurrencyChange:
            onCurrencySelected((effect as! SelectCurrencyEffect.CurrencyChange).newBase)
            isBarShown = false
        case is SelectCurrencyEffect.OpenCurrencies:
            navigationStack.push(CurrenciesView(onBaseChange: onCurrencySelected))
        default:
            logger.i(message: {"BarView unknown effect"})
        }
    }
}
