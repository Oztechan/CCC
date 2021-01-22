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

    @Environment(\.koin) var koin: Koin

    @Environment(\.colorScheme) var colorScheme

    @ObservedObject
    var vmWrapper: CurrenciesVMWrapper

    @State var isAlertShown = false

    var baseCurrencyChangeEffect: ((String) -> Void)?

    init(viewModel: CurrenciesViewModel, baseCurrencyChangeEffect: ((String) -> Void)? = nil) {
        self.vmWrapper = CurrenciesVMWrapper(viewModel: viewModel)
        self.baseCurrencyChangeEffect = baseCurrencyChangeEffect
        LoggerKt.kermit.d(withMessage: {"CurrenciesView init"})

        UITableView.appearance().tableHeaderView = UIView(
            frame: CGRect(
                x: 0,
                y: 0,
                width: 0,
                height: Double.leastNonzeroMagnitude
            )
        )
        UITableView.appearance().backgroundColor = MR.colors().background.get()
    }

    var body: some View {
        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {

                CurrencyToolbarView(
                    firstRun: vmWrapper.viewModel.isFirstRun(),
                    onCloseClick: { vmWrapper.viewModel.event.onCloseClick() },
                    updateAllCurrenciesState: { vmWrapper.viewModel.event.updateAllCurrenciesState(state: $0) }
                )

                if vmWrapper.state.loading {
                    ProgressView()
                }

                Form {
                    List(vmWrapper.state.currencyList, id: \.name) { currency in
                        CurrencyItemView(
                            item: currency,
                            onItemClick: { vmWrapper.viewModel.event.onItemClick(currency: currency) }
                        )
                    }
                    .id(UUID())
                    .listRowBackground(MR.colors().background.get())
                }

                if vmWrapper.viewModel.isFirstRun() {
                    HStack {

                        Text(MR.strings().txt_select_currencies.get())
                            .foregroundColor(MR.colors().text.get())
                            .font(.subheadline)
                        Spacer()
                        Button(
                            action: { vmWrapper.viewModel.event.onDoneClick() },
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
        .background(MR.colors().background_strong.get())
        .onAppear { vmWrapper.startObserving() }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { vmWrapper.stopObserving() }
    }

    private func onEffect(effect: CurrenciesEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        case is CurrenciesEffect.FewCurrency:
            isAlertShown = true
        case is CurrenciesEffect.OpenCalculator:
            UIApplication.shared.windows.first(where: \.isKeyWindow)?.rootViewController = UIHostingController(
                rootView: CalculatorView(viewModel: koin.get())
            )
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

struct CurrenciesViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        CurrenciesView(viewModel: koin.get())
    }
}
