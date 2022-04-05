//
//  File.swift
//  CCC
//
//  Created by Mustafa Ozhan on 30/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftMessages
import SwiftUI
import Client

func showSnack(
    text: String,
    buttonText: String? = nil,
    action: (() -> Void)? = nil,
    iconImage: UIImage = MR.images().ic_app_logo.get()
) {

    let view = MessageView.viewFromNib(layout: .cardView)
    view.configureTheme(
        backgroundColor: MR.colors().background_weak.get(),
        foregroundColor: MR.colors().text.get(),
        iconImage: iconImage.resized(
            to: CGSize(width: 64, height: 64)
        )
    )

    view.configureDropShadow()

    view.configureContent(
        title: "",
        body: text,
        iconImage: iconImage.resized(to: CGSize(width: 64, height: 64)),
        iconText: nil,
        buttonImage: nil,
        buttonTitle: buttonText,
        buttonTapHandler: { _ in
            logger.i(message: {"Snackbar buttonTab"})
            action?()
        })

    if buttonText != nil {
        view.button?.contentEdgeInsets = UIEdgeInsets(
            top: 10.0,
            left: 10.0,
            bottom: 10.0,
            right: 10.0
        )

        view.button?.backgroundColor = MR.colors().primary.get()
    } else {
        view.button?.isHidden = true
    }

    var config = SwiftMessages.defaultConfig
    config.presentationStyle = .bottom
    config.presentationContext = .window(windowLevel: UIWindow.Level.normal)

    SwiftMessages.show(config: config, view: view)
}

extension UIImage {
    func resized(to size: CGSize) -> UIImage {
        return UIGraphicsImageRenderer(size: size).image { _ in
            draw(in: CGRect(origin: .zero, size: size))
        }
    }
}
