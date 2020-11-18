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

struct MainView: View {
    var mainViewModel: MainViewModel

    init(mainViewModel: MainViewModel) {
        self.mainViewModel = mainViewModel
        CommonKt.kermit.d(withMessage: {"MainView"})
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
        MainView(mainViewModel: koin.getMainViewModel())
            .makeForPreviewProvider()
    }
}
#endif
