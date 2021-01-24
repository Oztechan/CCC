//
//  CurrenciesView.swift
//  ios
//
//  Created by Mustafa Ozhan on 21/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct CurrenciesView: View {

    @Environment(\.colorScheme) var colorScheme
    @ObservedObject var vmWrapper: CurrenciesVMWrapper = Koin.shared.currenciesVMWrapper

    @State var isAlertShown = false

    @Binding var currenciesNavigationToogle: Bool

    init(currenciesNavigationToogle: Binding<Bool>) {
        LoggerKt.kermit.d(withMessage: {"CurrenciesView init"})

        self._currenciesNavigationToogle = currenciesNavigationToogle

        UITableView.appearance().tableHeaderView = UIView(frame: CGRect(
            x: 0,
            y: 0,
            width: 0,
            height: Double.leastNonzeroMagnitude
        ))
        UITableView.appearance().backgroundColor = MR.colors().transparent.get()
    }

    var body: some View {
        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {

                CurrencyToolbarView(
                    firstRun: vmWrapper.viewModel.isFirstRun(),
                    onCloseClick: { vmWrapper.event.onCloseClick() },
                    updateAllCurrenciesState: { vmWrapper.event.updateAllCurrenciesState(state: $0) }
                )

                Form {
                    if vmWrapper.state.loading {
                        HStack {
                            Spacer()
                            ProgressView().transition(.slide)
                            Spacer()
                        }
                        .listRowBackground(MR.colors().background.get())
                    } else {
                        List(vmWrapper.state.currencyList, id: \.name) { currency in
                            CurrencyItemView(
                                item: currency,
                                onItemClick: { vmWrapper.event.onItemClick(currency: currency) }
                            )
                        }
                        .id(UUID())
                        .listRowBackground(MR.colors().background.get())
                    }
                }.background(MR.colors().background.get())

                if vmWrapper.viewModel.isFirstRun() {
                    HStack {

                        Text(MR.strings().txt_select_currencies.get())
                            .foregroundColor(MR.colors().text.get())
                            .font(.subheadline)
                        Spacer()
                        Button(
                            action: { vmWrapper.event.onDoneClick() },
                            label: {
                                Text(MR.strings().btn_done.get())
                                    .foregroundColor(MR.colors().text.get())

                            }
                        )
                        .padding(EdgeInsets(top: 10, leading: 15, bottom: 10, trailing: 15))
                        .background(MR.colors().background_weak.get())

                    }
                    .padding(EdgeInsets(top: 0, leading: 10, bottom: 10, trailing: 10))
                }

            }
            .navigationBarHidden(true)
        }
        .alert(isPresented: $isAlertShown) {
            Alert(
                title: Text(MR.strings().choose_at_least_two_currency.get()),
                dismissButton: .default(Text(MR.strings().txt_ok.get()))
            )
        }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: CurrenciesEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        case is CurrenciesEffect.FewCurrency:
            isAlertShown = true
        case is CurrenciesEffect.OpenCalculator:
            UIApplication.shared.windows.first(where: \.isKeyWindow)?.rootViewController = UIHostingController(
                rootView: CalculatorView()
            )
        case is CurrenciesEffect.Back:
            currenciesNavigationToogle.toggle()
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

struct CurrencyToolbarView: View {
    var firstRun: Bool
    var onCloseClick: () -> Void
    var updateAllCurrenciesState: (Bool) -> Void

    var body: some View {
        HStack {

            if !firstRun {
                Button(
                    action: onCloseClick,
                    label: {
                        Image(systemName: "chevron.left")
                            .imageScale(.large)
                            .accentColor(MR.colors().text.get())
                            .padding(.leading, 20)
                    }
                ).padding(.trailing, 10)

            }

            Text(MR.strings().txt_currencies.get())
                .font(.title2)

            Spacer()
            Button(
                action: { updateAllCurrenciesState(true) },
                label: { Text(MR.strings().btn_select_all.get()).foregroundColor(MR.colors().text.get()) }
            ).padding(.trailing, 10)
            Button(
                action: { updateAllCurrenciesState(false) },
                label: { Text(MR.strings().btn_de_select_all.get()).foregroundColor(MR.colors().text.get()) }
            )

        }.padding(EdgeInsets(top: 20, leading: 10, bottom: 5, trailing: 20))
    }
}

struct CurrencyItemView: View {
    @State var item: Currency

    var onItemClick: () -> Void

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
            Image(systemName: item.isActive ? "checkmark.circle.fill" : "circle")

        }
        .contentShape(Rectangle())
        .onTapGesture { onItemClick() }
        .lineLimit(1)
    }
}
