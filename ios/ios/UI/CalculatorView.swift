//
//  MainView.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import UIKit
import client

struct CalculatorView: View {

    @ObservedObject
    var calculatorVMWrapper: CalculatorVMWrapper

    init(calculatorVMWrapper: CalculatorVMWrapper) {
        self.calculatorVMWrapper = calculatorVMWrapper
        LoggerKt.kermit.d(withMessage: {"CalculatorView init"})
    }

    var body: some View {
        VStack {
            Text(MR.strings().nsBundle.localizedString(forKey: "app_name", value: nil, table: nil))
            Text(MR.strings().app_name.bundle.localizedString(forKey: "app_name", value: nil, table: nil))
            Text(StringsKt.getStringDesc().localized())
            Text(StringsKt.getResourceStringDesc().localized())
            Text(MR.strings().app_name.localize())
            Text(LocalizedStringKey(MR.strings().app_name.resourceId))
        }
        .onAppear {
            calculatorVMWrapper.observeStates()
            calculatorVMWrapper.observeEffect()
        }
        .onReceive(calculatorVMWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { calculatorVMWrapper.stopObserving() }
    }

    private func onEffect(effect: CalculatorEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

extension ResourcesStringResource {
    func localize() -> String {
        return ResourcesResourceStringDesc(stringRes: self).localized()
    }
}

#if DEBUG
struct MainViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        CalculatorView(
            calculatorVMWrapper: CalculatorVMWrapper(viewModel: koin.get())
        ).makeForPreviewProvider()
    }
}
#endif
