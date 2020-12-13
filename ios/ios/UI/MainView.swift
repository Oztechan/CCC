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
    var settingsRepository: SettingsRepository

    init(settingsRepository: SettingsRepository) {
        self.settingsRepository = settingsRepository
        CommonKt.kermit.d(withMessage: {"MainView"})
    }

    var body: some View {
        if(settingsRepository.firstRun){
            Text("First run")
        } else {
            Text("Not first run")
        }
    }
}

#if DEBUG
struct MainViewPreviews: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        MainView(settingsRepository: koin.getSettingsRepository())
            .makeForPreviewProvider()
    }
}
#endif
