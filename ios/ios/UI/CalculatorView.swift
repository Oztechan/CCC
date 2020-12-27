//
//  MainView.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client
import common

struct CalculatorView: View {
    
    @ObservedObject
    var manager: CalculatorManager
    
    var body: some View {
        VStack{
        }
        .onAppear {
            manager.observeStates()
            manager.observeEffect()
        }
        .onReceive(manager.effect) { onEffect(effect: $0) }
        .onDisappear() { manager.stopObserving() }
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
        CalculatorView(
            manager: CalculatorManager(vm: koin.getCalculatorViewModel())
        ).makeForPreviewProvider()
    }
}
#endif
