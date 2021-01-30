//
//  File.swift
//  ios
//
//  Created by Mustafa Ozhan on 30/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftMessages
import SwiftUI
import client

func toast(text: String) {

    let success = MessageView.viewFromNib(layout: .cardView)
    success.configureTheme(
        backgroundColor: MR.colors().background_weak.get(),
        foregroundColor: MR.colors().text.get(),
        iconImage: MR.images().ic_app_logo.get()
            .resized(to: CGSize(width: 64, height: 64)),
        iconText: nil
    )
    success.configureDropShadow()
    success.configureContent(title: "", body: text)
    success.button?.isHidden = true
    var successConfig = SwiftMessages.defaultConfig
    successConfig.presentationStyle = .bottom
    successConfig.presentationContext = .window(windowLevel: UIWindow.Level.normal)

    SwiftMessages.show(config: successConfig, view: success)
}

extension UIImage {
    func resized(to size: CGSize) -> UIImage {
        return UIGraphicsImageRenderer(size: size).image { _ in
            draw(in: CGRect(origin: .zero, size: size))
        }
    }
}
