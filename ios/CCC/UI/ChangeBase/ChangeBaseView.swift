//
//  BarView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Resources
import Client
import NavigationStack

typealias ChangeBaseObservable = ObservableSEED
<ChangeBaseViewModel, ChangeBaseState, ChangeBaseEffect, ChangeBaseEvent, BaseData>

struct ChangeBaseView: View {

    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStack
    @StateObject var observable: ChangeBaseObservable = koin.get()
    @Binding var isBarShown: Bool

    var onBaseChange: (String) -> Void

    var body: some View {

        NavigationView {

            ZStack {

                Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                if observable.state.currencyList.count < 2 {

                    SelectCurrencyView(
                        text: MR.strings().choose_at_least_two_currency.get(),
                        buttonText: MR.strings().select.get(),
                        onButtonClick: observable.event.onSelectClick
                    ).listRowBackground(MR.colors().background.get())

                } else {

                    Form {
                        if observable.state.loading {
                            FormProgressView()
                        } else {

                            List(observable.state.currencyList, id: \.name) { currency in

                                ChangeBaseItemView(item: currency)
                                    .onTapGesture { observable.event.onItemClick(currency: currency) }
                                    .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)

                            }.listRowInsets(.init())
                            .listRowBackground(MR.colors().background.get())
                        }
                    }
                    .background(MR.colors().background.get())
                    .navigationBarTitle(MR.strings().txt_select_base_currency.get())
                }
            }
        }
        .onAppear { observable.startObserving() }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: ChangeBaseEffect) {
        logger.i(message: {"ChangeBaseView onEffect \(effect.description)"})
        switch effect {
        // swiftlint:disable force_cast
        case is ChangeBaseEffect.BaseChange:
            onBaseChange((effect as! ChangeBaseEffect.BaseChange).newBase)
            isBarShown = false
        case is ChangeBaseEffect.OpenCurrencies:
            navigationStack.push(CurrenciesView(onBaseChange: onBaseChange))
        default:
            logger.i(message: {"BarView unknown effect"})
        }
    }
}
