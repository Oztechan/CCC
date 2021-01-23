//
//  SettingsVMWrapper.swift
//  ios
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Combine
import client

final class SettingsVMWrapper: VMWrapper {

    let viewModel: SettingsViewModel

    @Published var state = SettingsState(
        activeCurrencyCount: 0,
        appThemeType: AppTheme.systemDefault,
        addFreeDate: ""
    )

    var effect = PassthroughSubject<SettingsEffect, Never>()

    var event: SettingsEvent

    init(viewModel: SettingsViewModel) {
        self.viewModel = viewModel
        self.event = viewModel.event
        LoggerKt.kermit.d(withMessage: {"SettingsVMWrapper init"})
    }

    func startObserving() {
        self.viewModel.observeState(viewModel.state, provideNewState: { newState in
            if let state = newState as? SettingsState {
                self.state = state
            }
        })
        self.viewModel.observeEffect(viewModel.effect, provideNewEffect: { newEffect in
            if let effect = newEffect as? SettingsEffect {
                self.effect.send(effect)
            }
        })
    }

    func stopObserving() {
        self.viewModel.onCleared()
    }
}
