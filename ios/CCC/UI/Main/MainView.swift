//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Provider
import NavigationStack

struct MainView: View {
    let state: MainState

    var body: some View {
        NavigationStackView(
            transitionType: .default,
            easing: Animation.easeInOut
        ) {
            if state.shouldOnboardUser {
                IntroSlideRootView()
            } else {
                CalculatorRootView()
            }
        }
    }
}
