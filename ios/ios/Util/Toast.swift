//
//  Toast.swift
//  ios
//
//  Created by Mustafa Ozhan on 30/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Loaf
import SwiftUI
import client

func showToast(text: String) {
    Loaf(
        text,
        state: .custom(
            .init(
                backgroundColor: MR.colors().text.get(),
                textColor: MR.colors().background_weak.get(),
                tintColor: MR.colors().background_weak.get(),
                icon: UIImage(systemName: "info.circle.fill"),
                iconAlignment: .left
            )
        ),
        sender: UIApplication.shared.windows.first(where: \.isKeyWindow)!.rootViewController!
    ).show()
}
