//
//  MainView.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client

struct MainView: View {
    var mainViewModel: MainViewModel
    var kermit: Kermit

    init(mainViewModel: MainViewModel, kermit: Kermit) {
        self.mainViewModel = mainViewModel
        self.kermit = kermit
        kermit.d(withMessage: {"MainView"})
    }

    var body: some View {
        Text(mainViewModel.getAppName())
        Text(mainViewModel.getPlatformName())
        Text("\(mainViewModel.runCounter) times ran")
    }
}

#if DEBUG
struct MainViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        MainView(
            mainViewModel: koin.getMainViewModel(),
            kermit: koin.getKermit()
        ).makeForPreviewProvider()
    }
}
#endif
