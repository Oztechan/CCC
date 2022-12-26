//
//  Alert.swift
//  CCC
//
//  Created by Mustafa Ozhan on 13.08.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftMessages
import Res
import UIKit

func showAlert(
    title: String = "",
    text: String,
    buttonText: String,
    action:@escaping () -> Void = {} ,
    cancelable: Bool = true,
    hideOnAction: Bool = true
) {
    let view: MessageView = MessageView.viewFromNib(layout: .centeredView)
    view.configureBackgroundView(width: 250.cp())
    view.configureTheme(
        backgroundColor: MR.colors().background.get(),
        foregroundColor: MR.colors().text.get()
    )
    view.configureContent(
        title: title,
        body: text,
        iconImage: MR.images().ic_app_logo.get().resized(
            to: CGSize(width: 64.cp(), height: 64.cp())
        ),
        iconText: nil,
        buttonImage: nil,
        buttonTitle: buttonText
    ) { _ in
        action()

        if hideOnAction {
            SwiftMessages.hide()
        }
    }

    view.backgroundView.backgroundColor = MR.colors().background_weak.get()
    view.backgroundView.layer.cornerRadius = 10.cp()

    view.titleLabel?.font = view.bodyLabel?.font.withSize(18.cp())
    view.bodyLabel?.font = view.bodyLabel?.font.withSize(15.cp())
    view.button?.titleLabel?.font = view.bodyLabel?.font.withSize(15.cp())

    let cancelButton = UIButton(frame: CGRect(x: 100.cp(), y: 100.cp(), width: 100.cp(), height: 50.cp()))

    var config = SwiftMessages.defaultConfig
    config.presentationStyle = .center
    config.duration = .forever
    config.dimMode = .blur(style: .dark, alpha: 1, interactive: cancelable)
    config.presentationContext  = .window(windowLevel: UIWindow.Level.statusBar)
    SwiftMessages.show(config: config, view: view)
}
