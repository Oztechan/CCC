//
//  CalculatorManager.swift
//  ios
//
//  Created by Mustafa Ozhan on 26/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import Combine
import client

final class CalculatorManager: BaseManager {

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

    var effect = PassthroughSubject<CalculatorEffect, Never>()

    init(viewModel: CalculatorViewModel) {
        self.viewModel = viewModel
        LoggerKt.kermit.d(withMessage: {"CalculatorManager init"})
    }

    func observeEffect() {
        self.viewModel.observeEffect(viewModel.effect, provideNewEffect: { newEffect in
            if let effect = newEffect as? CalculatorEffect {
                self.effect.send(effect)
            }
        })
    }

    func observeStates() {
        self.viewModel.observeState(viewModel.state, provideNewState: { newState in
            if let state = newState as? CalculatorState {
                self.state = state
            }
        })
    }

    func stopObserving() {
        self.viewModel.onCleared()
    }
}
