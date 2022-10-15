//
//  ObservableSEED.swift
//  CCC
//
//  Created by Mustafa Ozhan on 25/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Combine
import Provider

final class ObservableSEEDViewModel<
    State: BaseState,
    Effect: BaseEffect,
    Event: BaseEvent,
    Data: BaseData,
    ViewModel: BaseSEEDViewModel<State, Effect, Event, Data>
>: ObservableObject {

    let viewModel: ViewModel = koin.get()

    @Published private(set) var state: State

    let effect = PassthroughSubject<Effect, Never>()
    let event: Event

    let data: Data?

    private var closeable: RuntimeCloseable!

    // swiftlint:disable force_cast
    init() {
        logger.i(message: {"ObservableSEED \(ViewModel.description()) init"})

        self.state = State()
        self.event = viewModel.event!
        self.data = viewModel.data
    }

    deinit {
        viewModel.onCleared()
    }

    func startObserving() {
        logger.i(message: {"ObservableSEED \(ViewModel.description()) startObserving"})

        if viewModel.state != nil {
            closeable = IOSCoroutineUtilKt.observeWithCloseable(viewModel.state!, onChange: {
                self.state = $0 as! State
            })
        }
        if viewModel.effect != nil {
            closeable = IOSCoroutineUtilKt.observeWithCloseable(viewModel.effect!, onChange: {
                self.effect.send($0 as! Effect)
            })
        }
    }

    func stopObserving() {
        logger.i(message: {"ObservableSEED \(ViewModel.description()) stopObserving"})
        closeable.close()
    }
}
