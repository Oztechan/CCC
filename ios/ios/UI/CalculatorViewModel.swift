//
//  CalculatorViewModel.swift
//  ios
//
//  Created by Mustafa Ozhan on 25/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import Combine
import client

final class CalculatorViewModel: BaseViewModel {
    let useCase: CalculatorUseCase
    
    @Published var state: CalculatorState
    var effect = PassthroughSubject<CalculatorEffect, Never>()
    
    
    init(useCase: CalculatorUseCase) {
        self.useCase = useCase
        self.state = self.useCase.state
    }
    
    func startObserving() {
//        self.useCase.onChange(self.useCase.state as! Kotlinx_coroutines_coreFlow, provideNewState: { newState in
//            self.state = newState as! CalculatorState
//        })
        
        self.useCase.onChange(self.useCase.effect, provideNewState: { newState in
            self.effect.send(newState as! CalculatorEffect)
        })
    }

    func stopObserving() {
        self.useCase.onDestroy()
    }
}
