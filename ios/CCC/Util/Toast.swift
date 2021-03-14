//
//  Toast.swift
//  CCC
//
//  Created by Mustafa Ozhan on 11/03/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Loaf
import Client

func showToast(text: String) {
    Loaf(
        text,
        state: .custom(
            .init(
                backgroundColor: MR.colors().text.get(),
                textColor: MR.colors().background_weak.get(),
                tintColor: MR.colors().background_weak.get(),
                icon: UIImage(systemName: "info.circle.fill"),
                textAlignment: .left,
                iconAlignment: .left,
                width: .screenPercentage(getPercentageFromString(text: text))
            )
        ),
        sender: UIApplication.shared.windows.first(where: \.isKeyWindow)!.rootViewController!
    ).show()
}

private func getPercentageFromString(text: String) -> CGFloat {
    switch text.count {
    case 0...15:
        return 0.4
    case 15...30:
        return 0.5
    case 30...40:
        return 0.6
    case 40...50:
        return 0.7
    case 50...60:
        return 0.75
    default:
        return 0.8
    }
}
