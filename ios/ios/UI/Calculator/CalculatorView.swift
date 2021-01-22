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

    @ObservedObject
    var vmWrapper: CalculatorVMWrapper

    @State var isBarShown = false
    @State var isAlertShown = false

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

//                    CalculationInputView(
//                        input: vmWrapper.state.input,
//                        destinationView: CurrenciesView(baseCurrencyChangeEffect: { newBase in
//                            vm.event.baseCurrencyChangeEvent(newBase: newBase)
//                        })
//                    )

                    CalculationOutputView(
                        baseCurrency: vmWrapper.state.base,
                        output: vmWrapper.state.output,
                        barClickEvent: {vmWrapper.viewModel.event.onBarClick()}
                    )

                    if vmWrapper.state.loading {
                        ProgressView()
                    }

                    Form {
                        List(
                            ExtensionsKt.toValidList(vmWrapper.state.currencyList, currentBase: vmWrapper.state.base),
                            id: \.rate
                        ) {
                            CalculatorItemView(
                                item: $0,
                                itemClickEvent: { item in
                                    vmWrapper.viewModel.event.onItemClick(
                                        currency: item,
                                        conversion: ExtensionsKt.toStandardDigits(
                                            IOSExtensionsKt.getFormatted(item.rate)
                                        )
                                    )
                                }
                            )
                        }.listRowBackground(MR.colors().background.get())
                    }

                    KeyboardView(keyPressEvent: { key in vmWrapper.viewModel.event.onKeyPress(key: key) })

                }
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
//        ).alert(isPresented: $isAlertShown) {
//            Alert(
//                title: Text(vm.data.alertText),
//                dismissButton: .default(Text("OK"))
//            )
//        }
        .onAppear { vmWrapper.startObserving() }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { vmWrapper.stopObserving() }
    }

    private func onEffect(effect: CalculatorEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.MaximumInput, is CalculatorEffect.FewCurrency:
            isAlertShown = true
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
