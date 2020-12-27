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
import common

final class CalculatorManager: BaseManager {

    let viewModel: CalculatorViewModel

    @Published var state = State()
    var effect = PassthroughSubject<CalculatorEffect, Never>()

    init(viewModel: CalculatorViewModel) {
        self.viewModel = viewModel
        LoggerKt.kermit.d(withMessage: {"init CalculatorManager"})

    }

    func observeEffect() {
        self.viewModel.observeEffect(viewModel.effect, provideNewEffect: { newEffect in
            if let effect = newEffect as? CalculatorEffect {
                self.effect.send(effect)
            }
        })
    }

    func observeStates() {

        LoggerKt.kermit.d(withMessage: {"observeStates"})

        self.viewModel.observeState(viewModel.state.base, provideNewState: { newState in
            if let state = newState as? String {
                self.state.base = state
            }
        })
        self.viewModel.observeState(viewModel.state.currencyList, provideNewState: { newState in
            if let state = (newState as? NSMutableArray)?.compactMap({ $0 as? CommonCurrency }) {
                self.state.currencyList = state
            }
        })
        self.viewModel.observeState(viewModel.state.dataState, provideNewState: { newState in
            if let state = newState as? DataState {
                self.state.dataState = state
            }
        })
        self.viewModel.observeState(viewModel.state.input, provideNewState: { newState in
            if let state = newState as? String {
                self.state.input = state
            }
        })
        self.viewModel.observeState(viewModel.state.loading, provideNewState: { newState in
            if let state = newState as? Bool {
                self.state.loading = state
            }
        })
        self.viewModel.observeState(viewModel.state.output, provideNewState: { newState in
            if let state = newState as? String {
                self.state.output = state
            }
        })
        self.viewModel.observeState(viewModel.state.symbol, provideNewState: { newState in
            if let state = newState as? String {
                self.state.symbol = state
            }
        })
    }

    func stopObserving() {
        LoggerKt.kermit.d {"stopObserving"}
        self.viewModel.onCleared()
    }

    struct State {
        var base = ""
        var currencyList = [CommonCurrency]()
        var dataState: DataState = DataState.Error()
        var input = ""
        var loading = true
        var output = ""
        var symbol = ""
    }
}
