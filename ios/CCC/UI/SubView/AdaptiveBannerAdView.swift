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

struct AdaptiveBannerAdView: UIViewControllerRepresentable {

    private var unitID: String

    private let adSize = GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth(
        UIScreen.main.bounds.size.width
    )

    init(unitID: String) {
        self.unitID = SecretUtil.getSecret(key: unitID)
    }

    let bannerView = GADBannerView(adSize: GADAdSizeBanner)

    func makeUIViewController(context: Context) -> UIViewController {

        let viewController = UIViewController()

        bannerView.adUnitID = unitID
        bannerView.rootViewController = viewController

        viewController.view.addSubview(bannerView)
        viewController.view.frame = CGRect(origin: .zero, size: adSize.size)

        bannerView.adSize = adSize
        bannerView.load(GADRequest())

        return viewController
    }

    func adjust() -> some View {
        let padding = UIScreen.main.focusedView?.safeAreaInsets.bottom ?? 0
        return self
            .frame(maxHeight: adSize.size.height)
            .padding(.bottom, padding)
    }

    func updateUIViewController(
        _ uiViewController: UIViewController,
        context: Context
    ) {}
}
