//
//  VMObservable.swift
//  ios
//
//  Created by Mustafa Ozhan on 26/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Combine
import Client

final class ObservableVM<ViewModel: BaseViewModel>: ObservableObject {

    let viewModel: ViewModel

    init(viewModel: ViewModel) {
        LoggerKt.kermit.d(withMessage: {"ObservableVM \(ViewModel.description()) init"})
        self.viewModel = viewModel
    }

    deinit {
        self.viewModel.onCleared()
    }
}
