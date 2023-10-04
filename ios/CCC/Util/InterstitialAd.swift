//
//  InterstitialAd.swift
//  CCC
//
//  Created by Mustafa Ozhan on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds

final class InterstitialAd: NSObject, GADFullScreenContentDelegate {
    func show() {
        GADInterstitialAd.load(
            withAdUnitID: SecretUtil.getSecret(key: "INTERSTITIAL_AD_ID"),
            request: GADRequest(),
            completionHandler: { interstitialAd, error in
                if let error = error {
                    logger.e(message: { "InterstitialAd show \(error.localizedDescription)" })
                    return
                }

                if UIApplication.shared.applicationState == .active {
                    interstitialAd?.fullScreenContentDelegate = self
                    interstitialAd?.present(
                        fromRootViewController: WindowUtil.getCurrentController()
                    )
                } else {
                    logger.v(message: { "InterstitialAd not showed appState is not active" })
                }
            }
        )
    }
}
