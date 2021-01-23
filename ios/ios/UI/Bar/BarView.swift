//
//  BarView.swift
//  ios
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct BarView: View {

    @ObservedObject var vmWrapper: BarVMWrapper
    @Binding var isBarShown: Bool

    init(
        viewModel: BarViewModel,
        isBarShown: Binding<Bool>
    ) {
        self.vmWrapper = BarVMWrapper(viewModel: viewModel)
        self._isBarShown = isBarShown
        LoggerKt.kermit.d(withMessage: {"BarView init"})
    }

    var body: some View {

        NavigationView {

            if vmWrapper.state.loading {
                ProgressView()
            }

            Form {

                List(vmWrapper.state.currencyList, id: \.name) { currency in

                    BarItemView(item: currency)
                        .onTapGesture { vmWrapper.viewModel.event.onItemClick(currency: currency) }
                        .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)

                }.listRowBackground(Color("ColorBackground"))

            }.navigationBarTitle("Base Currency")

        }
        .onAppear { vmWrapper.startObserving() }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { vmWrapper.stopObserving() }
    }

    private func onEffect(effect: BarEffect) {
        switch effect {
        case is BarEffect.ChangeBaseNavResult:
            LoggerKt.kermit.d(withMessage: {"BarEffect.ChangeBaseNavResult"})
        case is BarEffect.OpenCurrencies:
            isBarShown = false
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

#if DEBUG
struct BarViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        BarView(viewModel: koin.get(), isBarShown: .constant(true)).makeForPreviewProvider()
    }
}
#endif
