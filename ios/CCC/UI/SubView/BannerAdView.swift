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
          // Here safe area is taken into account, hence the view frame is used
          // after the view has been laid out.
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

        // Step 4 - Create an ad request and load the adaptive banner ad.
        bannerView.load(GADRequest())

        return viewController
    }

    func updateUIViewController(
        _ uiViewController: UIViewController,
        context: Context
    ) {}
}
