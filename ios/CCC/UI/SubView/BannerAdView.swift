//
//  BannerVC.swift
//  CCC
//
//  Created by Mustafa Ozhan on 05/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import GoogleMobileAds
import UIKit

final class BannerAdView: UIViewControllerRepresentable {

    private var unitID: String

    init(unitID: String) {
        self.unitID = unitID
    }

    let bannerView = GADBannerView(adSize: kGADAdSizeBanner)

    func makeUIViewController(context: Context) -> UIViewController {

        let viewController = UIViewController()

        bannerView.adUnitID = unitID
        bannerView.rootViewController = viewController

        let frame = { () -> CGRect in
          if #available(iOS 11.0, *) {
            return viewController.view.frame.inset(by: viewController.view.safeAreaInsets)
          } else {
            return viewController.view.frame
          }
        }()

        let viewWidth = frame.size.width

        viewController.view.addSubview(bannerView)
        viewController.view.frame = CGRect(origin: .zero, size: kGADAdSizeBanner.size)

        bannerView.adSize = GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth(viewWidth)
        bannerView.load(GADRequest())

        return viewController
    }

    func updateUIViewController(
        _ uiViewController: UIViewController,
        context: Context
    ) {}
}
