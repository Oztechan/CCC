//
//  MainView.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import UIKit
import client

struct CalculatorView: View {

    @Environment(\.colorScheme) var colorScheme
    @Environment(\.koin) var koin: Koin

    @ObservedObject
    var vmWrapper: CalculatorVMWrapper

    @State var isBarShown = false
    @State var fewCurrencyAlert = false
    @State var maximumInputAlert = false
    @State var navigation = false

    init(viewModel: CalculatorViewModel) {
        self.vmWrapper = CalculatorVMWrapper(viewModel: viewModel)
        LoggerKt.kermit.d(withMessage: {"CalculatorView init"})

        UITableView.appearance().tableHeaderView = UIView(frame: CGRect(
            x: 0,
            y: 0,
            width: 0,
            height: Double.leastNonzeroMagnitude
        ))
        UITableView.appearance().backgroundColor = MR.colors().background.get()
    }

    var body: some View {
        NavigationView {
            ZStack {
                Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                VStack {

                    CalculationInputView(
                        input: vmWrapper.state.input,
                        destinationView: CurrenciesView(viewModel: koin.get())
                    )

                    CalculationOutputView(
                        baseCurrency: vmWrapper.state.base,
                        output: vmWrapper.state.output,
                        symbol: vmWrapper.state.symbol,
                        onBarClick: {vmWrapper.viewModel.event.onBarClick()}
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

                            List(
                                ExtensionsKt.toValidList(
                                    vmWrapper.state.currencyList,
                                    currentBase: vmWrapper.state.base
                                ),
                                id: \.rate
                            ) {
                                CalculatorItemView(
                                    item: $0,
                                    onItemClick: { item in
                                        vmWrapper.viewModel.event.onItemClick(
                                            currency: item,
                                            conversion: ExtensionsKt.toStandardDigits(
                                                IOSExtensionsKt.getFormatted(item.rate)
                                            )
                                        )
                                    }
                                )
                            }
                            .listRowBackground(MR.colors().background.get())
                        }
                    }

                    KeyboardView(onKeyPress: { vmWrapper.viewModel.event.onKeyPress(key: $0) })

                }

                NavigationLink(
                    destination: CurrenciesView(viewModel: koin.get()),
                    isActive: $navigation
                ) { }.hidden()

            }
            .navigationBarHidden(true)
        }
//        .sheet(
//            isPresented: $isBarShown,
//            content: {
//                BarView(
//                    baseCurrencyChangeEvent: { newBase in
//                        vm.event.baseCurrencyChangeEvent(newBase: newBase)
//                    },
//                    isBarShown: $isBarShown
//                )
//            }
//        )
        .alert(isPresented: $maximumInputAlert) {
            Alert(
                title: Text(MR.strings().max_input.get()),
                dismissButton: .default(Text(MR.strings().txt_ok.get()))
            )
        }
        .alert(isPresented: $fewCurrencyAlert) {
            Alert(
                title: Text(MR.strings().txt_select_currencies.get()),
                primaryButton: .default(Text(MR.strings().txt_ok.get())) {
                    navigation.toggle()
                },
                secondaryButton: .cancel()
            )
        }
        .onAppear { vmWrapper.startObserving() }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { vmWrapper.stopObserving() }
    }

    private func onEffect(effect: CalculatorEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.MaximumInput:
            maximumInputAlert = true
        case is CalculatorEffect.FewCurrency:
            fewCurrencyAlert = true
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

#if DEBUG
struct MainViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        CalculatorView(viewModel: koin.get()).makeForPreviewProvider()
    }
}
#endif
