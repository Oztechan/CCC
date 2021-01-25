//
//  ObservableSEED.swift
//  ios
//
//  Created by Mustafa Ozhan on 25/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Combine
import client

class ObservableSEED<
    ViewModel: BaseSEEDViewModel,
    State: BaseState,
    Effect: BaseEffect,
    Event: BaseEvent
>: ObservableObject {

    let viewModel: ViewModel

    @Published var state: State

    var effect = PassthroughSubject<Effect, Never>()
    var event: Event

    // swiftlint:disable force_cast
    init(viewModel: ViewModel, state: State) {
        LoggerKt.kermit.d(withMessage: {"ObservableSEED \(ViewModel.description()) init"})

        self.viewModel = viewModel
        self.state = state
        self.event = viewModel.event as! Event

        self.viewModel.observeState(viewModel.state, provideNewState: {
            self.state = $0 as! State
        })
        self.viewModel.observeEffect(viewModel.effect, provideNewEffect: {
            self.effect.send($0 as! Effect)
        })
    }

    deinit {
        self.viewModel.onCleared()
    }
}
