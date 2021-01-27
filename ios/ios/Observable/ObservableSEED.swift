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
    Event: BaseEvent,
    Data: BaseData
>: ObservableObject {

    let viewModel: ViewModel

    @Published var state: State

    var effect = PassthroughSubject<Effect, Never>()
    var event: Event

    var data: Data

    // swiftlint:disable force_cast
    init(viewModel: ViewModel, state: State) {
        LoggerKt.kermit.d(withMessage: {"ObservableSEED \(ViewModel.description()) init"})

        self.viewModel = viewModel
        self.state = state
        self.event = viewModel.event as! Event
        self.data = viewModel.data as! Data
    }

    deinit {
        self.viewModel.onCleared()
    }

    func startObserving() {
        self.viewModel.observe(viewModel.state, onChange: {
            self.state = $0 as! State
        })
        self.viewModel.observe(viewModel.effect, onChange: {
            self.effect.send($0 as! Effect)
        })
    }
}
