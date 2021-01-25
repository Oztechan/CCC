//
//  CalculatorVMWrapper.swift
//  ios
//
//  Created by Mustafa Ozhan on 26/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Combine
import client

final class CalculatorVMWrapper: ObservableObject {

    let viewModel: CalculatorViewModel

    @Published var state = CalculatorState()

    var event: CalculatorEvent

    var effect = PassthroughSubject<CalculatorEffect, Never>()

    init(viewModel: CalculatorViewModel) {
        LoggerKt.kermit.d(withMessage: {"CalculatorVMWrapper init"})

        self.viewModel = viewModel
        self.event = viewModel.event

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

    deinit {
        self.viewModel.onCleared()
    }
}
