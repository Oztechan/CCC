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

    @ObservedObject
    var currenciesVMWrapper: CurrenciesVMWrapper

    init(viewModel: CurrenciesViewModel) {
        self.currenciesVMWrapper = CurrenciesVMWrapper(viewModel: viewModel)
        LoggerKt.kermit.d(withMessage: {"CurrenciesView init"})
    }

    var body: some View {
        VStack {

        Text("Currencies View").background(MR.colors().accent.get())
        }
        .onAppear { currenciesVMWrapper.startObserving() }
        .onReceive(currenciesVMWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { currenciesVMWrapper.stopObserving() }
    }

    private func onEffect(effect: CurrenciesEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
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
