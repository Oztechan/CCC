//
//  InterstitialAd.swift
//  CCC
//
//  Created by Mustafa Ozhan on 05/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import GoogleMobileAds
import UIKit
import Client

final class InterstitialAd: NSObject, GADFullScreenContentDelegate {
    var interstitial: GADInterstitialAd = GADInterstitialAd()

    override init() {
        super.init()

        let request = GADRequest()
        GADInterstitialAd.load(
            withAdUnitID: "INTERSTITIAL_AD_ID".getSecretValue(),
            request: request,
            completionHandler: { [self] interstitialAd, error in
                if let error = error {
                    print("Failed to load interstitial ad with error: \(error.localizedDescription)")
                    return
                }
                interstitial = interstitialAd!
                interstitial.fullScreenContentDelegate = self
            }
        )
    }

    /// Tells the delegate that the ad failed to present full screen content.
    func ad(_ interstitialAd: GADFullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
        LoggerKt.kermit.d(withMessage: {"Ad did fail to present full screen content."})
    }

    /// Tells the delegate that the ad presented full screen content.
    func adDidPresentFullScreenContent(_ interstitialAd: GADFullScreenPresentingAd) {
        LoggerKt.kermit.d(withMessage: {"Ad did present full screen content."})
    }

    /// Tells the delegate that the ad dismissed full screen content.
    func adDidDismissFullScreenContent(_ interstitialAd: GADFullScreenPresentingAd) {
        LoggerKt.kermit.d(withMessage: {"Ad did dismiss full screen content."})
    }

    func showAd() {
        interstitial.present(
            fromRootViewController: UIApplication.shared.windows.first!.rootViewController!
        )
    }
}
