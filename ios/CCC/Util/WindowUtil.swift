//
//  WindowUtil.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import UIKit

public struct WindowUtil {
    private static func getCurrentWindow() -> UIWindow? {
        return UIApplication.shared.connectedScenes
            .filter { $0.activationState == .foregroundActive }
            .first(where: { $0 is UIWindowScene })
            .flatMap({ $0 as? UIWindowScene })?.windows
            .first(where: \.isKeyWindow)
    }

    public static func getCurrentController() -> UIViewController {
        var viewController = getCurrentWindow()?.rootViewController

        if let presentedController = viewController as? UITabBarController {
            viewController = presentedController.selectedViewController
        }

        while let presentedController = viewController?.presentedViewController {
            if let presentedController = presentedController as? UITabBarController {
                viewController = presentedController.selectedViewController
            } else {
                viewController = presentedController
            }
        }
        return viewController!
    }
}
