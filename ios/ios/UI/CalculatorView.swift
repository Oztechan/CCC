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

    var calculatorViewModel: CalculatorViewModel

    var body: some View {
    }
}

#if DEBUG
struct MainViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        CalculatorView(calculatorViewModel: koin.getCalculatorViewModel())
            .makeForPreviewProvider()
    }
}
#endif
