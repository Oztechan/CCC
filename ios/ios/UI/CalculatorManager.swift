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
    let vm: CalculatorViewModel
    
    @Published var state = State()
    var effect = PassthroughSubject<CalculatorEffect, Never>()
    
    
    init(vm: CalculatorViewModel) {
        self.vm = vm
        LoggerKt.kermit.d(withMessage: {"init CalculatorManager"})

    }

    func observeEffect() {
        self.vm.observeEffect(vm.effect, provideNewEffect: { newEffect in
            self.effect.send(newEffect as! CalculatorEffect)
        })
    }
    
    func observeStates() {
        
        LoggerKt.kermit.d(withMessage: {"observeStates"})
        
        self.vm.observeState(vm.state.base, provideNewState: { newState in
            self.state.base = newState as! String
        })
        self.vm.observeState(vm.state.currencyList, provideNewState: { newState in
            self.state.currencyList = (newState as! NSMutableArray).compactMap { $0 as? CommonCurrency }
        })
        self.vm.observeState(vm.state.dataState, provideNewState: { newState in
            self.state.dataState = newState as! DataState
        })
        self.vm.observeState(vm.state.input, provideNewState: { newState in
            self.state.input = newState as! String
        })
        self.vm.observeState(vm.state.loading, provideNewState: { newState in
            self.state.loading = newState as! Bool
        })
        self.vm.observeState(vm.state.output, provideNewState: { newState in
            self.state.output = newState as! String
        })
        self.vm.observeState(vm.state.symbol, provideNewState: { newState in
            self.state.symbol = newState as! String
        })
    }
    
    func stopObserving() {
        LoggerKt.kermit.d {"stopObserving"}
        self.vm.onCleared()
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
