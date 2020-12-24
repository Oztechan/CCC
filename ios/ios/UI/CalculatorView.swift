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
    
    @ObservedObject var vm: CalculatorViewModel
    
    init(vm: CalculatorViewModel) {
        self.vm = vm
    }
    
    var body: some View {
        Text("test")
            .onAppear {
                self.vm.startObserving()
            }.onDisappear {
                self.vm.stopObserving()
            }
    }
}

#if DEBUG
struct MainViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin
    
    static var previews: some View {
        CalculatorView(
            vm: CalculatorViewModel(useCase: koin.getCalculatorUseCase())
        ).makeForPreviewProvider()
    }
}
#endif
