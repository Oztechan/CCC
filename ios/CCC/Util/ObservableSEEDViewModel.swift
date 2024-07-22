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
    ViewModel: SEEDViewModel<State, Effect, Event, Data>
>: ObservableObject {
    let viewModel: ViewModel = koin.get()

    @Published private(set) var state: State

    let effect = PassthroughSubject<Effect, Never>()
    let event: Event

    private var stateClosable: Closeable?
    private var effectClosable: Closeable?

    init() {
        logger.d(message: { "ObservableSEED \(ViewModel.description()) init" })

        // swiftlint:disable:next force_cast
        self.state = viewModel.state.value as! State
        self.event = viewModel.event!
    }

    deinit {
        closeClosables()
    }

    func startObserving() {
        logger.d(message: { "ObservableSEED \(ViewModel.description()) startObserving" })

        stateClosable = CoroutineUtilKt.observeWithCloseable(viewModel.state, onChange: {
            // swiftlint:disable:next force_cast
            self.state = $0 as! State
        })

        if viewModel.effect != nil {
            effectClosable = CoroutineUtilKt.observeWithCloseable(viewModel.effect!, onChange: {
                // swiftlint:disable:next force_cast
                self.effect.send($0 as! Effect)
            })
        }
    }

    func stopObserving() {
        logger.d(message: { "ObservableSEED \(ViewModel.description()) stopObserving" })
        closeClosables()
    }

    private func closeClosables() {
        stateClosable?.close()
        effectClosable?.close()
    }
}
