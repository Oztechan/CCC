//
//  CurrenciesVMWrapper.swift
//  ios
//
//  Created by Mustafa Ozhan on 21/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Combine
import client

final class CurrenciesVMWrapper: VMWrapper {

    let viewModel: CurrenciesViewModel

    @Published var state = CurrenciesState(
        currencyList: [Currency](),
        loading: true,
        selectionVisibility: false
    )

    var effect = PassthroughSubject<CurrenciesEffect, Never>()

    var event: CurrenciesEvent

    init(viewModel: CurrenciesViewModel) {
        self.viewModel = viewModel
        self.event = viewModel.event
        LoggerKt.kermit.d(withMessage: {"CurrenciesVMWrapper init"})
    }

    func startObserving() {
        self.viewModel.observeState(viewModel.state, provideNewState: { newState in
            if let state = newState as? CurrenciesState {
                self.state = state
            }
        })
        self.viewModel.observeEffect(viewModel.effect, provideNewEffect: { newEffect in
            if let effect = newEffect as? CurrenciesEffect {
                self.effect.send(effect)
            }
        })
    }

    func stopObserving() {
        self.viewModel.onCleared()
    }
}
