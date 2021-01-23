//
//  BarVMWrapper.swift
//  ios
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Combine
import client

final class BarVMWrapper: VMWrapper {

    let viewModel: BarViewModel

    @Published var state = BarState(
        currencyList: [Currency](),
        loading: true,
        enoughCurrency: false
    )

    var effect = PassthroughSubject<BarEffect, Never>()

    init(viewModel: BarViewModel) {
        self.viewModel = viewModel
        LoggerKt.kermit.d(withMessage: {"BarVMWrapper init"})
    }

    func startObserving() {
        self.viewModel.observeState(viewModel.state, provideNewState: { newState in
            if let state = newState as? BarState {
                self.state = state
            }
        })
        self.viewModel.observeEffect(viewModel.effect, provideNewEffect: { newEffect in
            if let effect = newEffect as? BarEffect {
                self.effect.send(effect)
            }
        })
    }

    func stopObserving() {
        self.viewModel.onCleared()
    }
}
