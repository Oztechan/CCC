//
//  MainVMWrapper.swift
//  ios
//
//  Created by Mustafa Ozhan on 21/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Combine
import client

final class MainVMWrapper: ObservableObject {

    var viewModel: MainViewModel

    init(viewModel: MainViewModel) {
        LoggerKt.kermit.d(withMessage: {"MainVMWrapper init"})
        self.viewModel = viewModel
    }

    deinit {
        self.viewModel.onCleared()
    }
}
