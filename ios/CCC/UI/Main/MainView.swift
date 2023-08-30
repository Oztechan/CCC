//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Provider

struct MainView: View {
    let state: MainState

    var body: some View {
        if state.shouldOnboardUser {
            IntroSlideRootView()
        } else {
            CalculatorRootView()
        }
    }
}
