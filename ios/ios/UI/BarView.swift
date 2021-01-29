//
//  BarView.swift
//  ios
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

typealias BarObservable = ObservableSEED
<BarViewModel, BarState, BarEffect, BarEvent, BaseData>

struct BarView: View {

    @Environment(\.colorScheme) var colorScheme
    @StateObject var observable: BarObservable = koin.get()
    @Binding var isBarShown: Bool
    @State var currenciesNavigationToggle: Bool = false

    var onDismiss: () -> Void

    var body: some View {

        NavigationView {

            ZStack {

                Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                if observable.state.currencyList.count < 2 {

                    SelectCurrencyView(
                        text: MR.strings().choose_at_least_two_currency.get(),
                        buttonText: MR.strings().select.get(),
                        onButtonClick: { currenciesNavigationToggle = true }
                    ).listRowBackground(MR.colors().background.get())

                } else {

                    Form {
                        if observable.state.loading {
                            FormProgressView()
                        } else {

                            List(observable.state.currencyList, id: \.name) { currency in

                                BarItemView(item: currency)
                                    .onTapGesture { observable.event.onItemClick(currency: currency) }
                                    .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)

                            }.listRowBackground(MR.colors().background.get())
                        }
                    }
                    .background(MR.colors().background.get())
                    .navigationBarTitle(MR.strings().txt_select_base_currency.get())
                }

                NavigationLink(
                    destination: CurrenciesView(currenciesNavigationToggle: $currenciesNavigationToggle),
                    isActive: $currenciesNavigationToggle
                ) { }.hidden()
            }
        }
        .onAppear {observable.startObserving()}
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: BarEffect) {
        switch effect {
        case is BarEffect.ChangeBase:
            onDismiss()
            isBarShown = false
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

struct BarItemView: View {
    @Environment(\.colorScheme) var colorScheme
    var item: Currency

    var body: some View {
        HStack {

            Image(uiImage: item.name.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)
            Text(item.name)
                .frame(width: 45)
                .foregroundColor(MR.colors().text.get())
            Text(item.longName)
                .font(.footnote)
                .foregroundColor(MR.colors().text.get())
            Text(item.symbol)
                .font(.footnote)
                .foregroundColor(MR.colors().text.get())
            Spacer()

        }
        .contentShape(Rectangle())
        .lineLimit(1)
    }
}
