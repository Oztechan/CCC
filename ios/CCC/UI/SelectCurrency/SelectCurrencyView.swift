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

                Color(Res.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                VStack {
                    Text(Res.strings().txt_select_base_currency.get())
                        .font(relative: .title2)
                        .padding(4.cp())
                        .padding(.top, 10.cp())

                    if observable.state.loading {
                        FormProgressView()
                    } else {
                        Form {
                            List(observable.state.currencyList, id: \.code) { currency in
                                SelectCurrencyItemView(item: currency)
                                    .onTapGesture { observable.event.onItemClick(currency: currency) }
                                    .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)
                            }
                            .listRowInsets(.init())
                            .listRowBackground(Res.colors().background.get())
                        }
                        .withClearBackground(color: Res.colors().background.get())
                    }

                    Spacer()

                    SelectCurrenciesBottomView(
                        text: observable.state.enoughCurrency ?
                        Res.strings().txt_update_favorite_currencies.get() :
                            Res.strings().choose_at_least_two_currency.get(),
                        buttonText: observable.state.enoughCurrency ?
                        Res.strings().update.get() :
                            Res.strings().select.get(),
                        onButtonClick: observable.event.onSelectClick
                    ).listRowBackground(Res.colors().background.get())
                }.navigationBarHidden(true)
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
