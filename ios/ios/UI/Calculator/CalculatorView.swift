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
//                        input: vm.state.input,
//                        destinationView: CurrenciesView(baseCurrencyChangeEffect: { newBase in
//                            vm.event.baseCurrencyChangeEvent(newBase: newBase)
//                        })
//                    )
//
//                    CalculationOutputView(
//                        baseCurrency: vm.state.baseCurrency,
//                        output: vm.state.output,
//                        barClickEvent: {vm.event.barClickEvent()}
//                    )
                    
                    if vmWrapper.state.loading {
                        ProgressView()
                    }
                    
//                    Form {
//                        List(
//                            vm.state.currencyList.filterResults(baseCurrency: vm.state.baseCurrency),
//                            id: \.value
//                        ) {
//                            CalculatorItemView(
//                                item: $0,
//                                itemClickEvent: { item in vm.event.itemClickEvent(item: item) }
//                            )
//                        }.listRowBackground(Color("ColorBackground"))
//                    }
                    
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
        case is OpenBarEffect:
            isBarShown = true
        case is MaximumInputEffect, is FewCurrencyEffect:
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
