//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct MainView: View {
    let isFirstRun: Bool

    var body: some View {
        if isFirstRun {
            IntroSlideRootView()
        } else {
            CalculatorRootView()
        }
    }
}
