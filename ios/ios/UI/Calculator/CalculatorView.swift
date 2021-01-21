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

    @Environment(\.colorScheme) var colorScheme

    @ObservedObject
    var vmWrapper: CalculatorVMWrapper

    init(viewModel: CalculatorViewModel) {
        self.vmWrapper = CalculatorVMWrapper(viewModel: viewModel)
        LoggerKt.kermit.d(withMessage: {"CalculatorView init"})
    }

    var body: some View {
        VStack {

            Text(MR.strings().app_name.get())
                .background(MR.colors().background.get())

            HStack {
                Image(uiImage: MR.images().tryy.get())
                Image(uiImage: "aed".getImage())
            }

        }
        .onAppear { vmWrapper.startObserving() }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { vmWrapper.stopObserving() }
    }

    private func onEffect(effect: CalculatorEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

#if DEBUG
struct MainViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        CalculatorView(viewModel: koin.get()).makeForPreviewProvider()
    }
}
#endif
