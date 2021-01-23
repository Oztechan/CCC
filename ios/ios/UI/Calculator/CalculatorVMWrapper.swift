//
//  CalculatorVMWrapper.swift
//  ios
//
//  Created by Mustafa Ozhan on 26/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Combine
import client

final class CalculatorVMWrapper: VMWrapper {

    let viewModel: CalculatorViewModel

    @Published var state = CalculatorState(
        input: "",
        base: "",
        currencyList: [Currency](),
        output: "",
        symbol: "",
        loading: true,
        dataState: DataState.Error()
    )

    var event: CalculatorEvent

    var effect = PassthroughSubject<CalculatorEffect, Never>()

    init(viewModel: CalculatorViewModel) {
        self.viewModel = viewModel
        self.event = viewModel.event
        LoggerKt.kermit.d(withMessage: {"CalculatorVMWrapper init"})
    }

    func startObserving() {
        self.viewModel.observeState(viewModel.state, provideNewState: { newState in
            if let state = newState as? CalculatorState {
                self.state = state
            }
        })
        self.viewModel.observeEffect(viewModel.effect, provideNewEffect: { newEffect in
            if let effect = newEffect as? CalculatorEffect {
                self.effect.send(effect)
            }
        })
    }

    func stopObserving() {
        self.viewModel.onCleared()
    }
}
