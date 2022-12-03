//
//  File.swift
//  CCC
//
//  Created by Mustafa Ozhan on 30/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftMessages
import SwiftUI
import Res

func showSnack(
    text: String,
    buttonText: String? = nil,
    action: (() -> Void)? = nil,
    iconImage: UIImage = MR.images().ic_app_logo.get(),
    isTop: Bool = false
) {
    SwiftMessages.hide(animated: false)

    let view = MessageView.viewFromNib(layout: .cardView)
    view.configureTheme(
        backgroundColor: MR.colors().background_weak.get(),
        foregroundColor: MR.colors().text.get(),
        iconImage: iconImage.resized(
            to: CGSize(width: 64.cp(), height: 64.cp())
        )
    )

    view.configureDropShadow()

    view.configureContent(
        title: "",
        body: text,
        iconImage: iconImage.resized(to: CGSize(width: 64.cp(), height: 64.cp())),
        iconText: nil,
        buttonImage: nil,
        buttonTitle: buttonText,
        buttonTapHandler: { _ in
            logger.i(message: {"Snackbar buttonTab"})
            action?()
        })

    view.bodyLabel?.font = view.bodyLabel?.font.withSize(15.cp())

    if buttonText != nil {
        view.button?.contentEdgeInsets = UIEdgeInsets(
            top: 10.cp(),
            left: 10.cp(),
            bottom: 10.cp(),
            right: 10.cp()
        )

        view.button?.backgroundColor = MR.colors().primary.get()
        view.button?.titleLabel?.font = view.bodyLabel?.font.withSize(12.cp())

    } else {
        view.button?.isHidden = true
    }

    var config = SwiftMessages.defaultConfig
    config.presentationStyle = isTop ? .top : .bottom
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
