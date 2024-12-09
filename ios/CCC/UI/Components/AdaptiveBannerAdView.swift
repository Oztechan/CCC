//
//  BannerVC.swift
//  CCC
//
//  Created by Mustafa Ozhan on 05/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import SwiftUI
import UIKit
import Provider

struct AdaptiveBannerAdView: UIViewControllerRepresentable {
    private var unitID: String

    private let adSize = GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth(
        UIScreen.main.bounds.size.width
    )

    init(unitID: String) {
        self.unitID = SecretUtil.getSecret(key: unitID)
    }

    private let bannerView = GADBannerView(adSize: GADAdSizeBanner)

    func makeUIViewController(context: Context) -> UIViewController {
        let viewController = UIViewController()

        bannerView.adUnitID = unitID
        bannerView.rootViewController = viewController

        // Set the delegate to the context coordinator
        bannerView.delegate = context.coordinator

        viewController.view.addSubview(bannerView)
        viewController.view.frame = CGRect(origin: .zero, size: adSize.size)

        bannerView.adSize = adSize
        bannerView.load(GADRequest())

        return viewController
    }

    func adapt() -> some View {
        return self
            .frame(maxHeight: adSize.size.height)
            .padding(.bottom, DeviceUtil.getBottomNotchHeight().cp())
    }

    func updateUIViewController(
        _ uiViewController: UIViewController,
        context: Context
    ) {
        // no impl
    }

    // SDK uses
    func makeCoordinator() -> AdaptiveBannerAdCoordinator {
        return AdaptiveBannerAdCoordinator()
    }

    class AdaptiveBannerAdCoordinator: NSObject, GADBannerViewDelegate {
        func bannerView(_ bannerView: GADBannerView, didFailToReceiveAdWithError error: Error) {
            let throwable = KotlinThrowable(
                message: "InterstitialAd show \(error.localizedDescription)"
            )
            logger.e(throwable: throwable, tag: logger.tag, message: { String(describing: throwable.message) })
        }
    }
}
