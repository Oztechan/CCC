//
//  InterstitialAd.swift
//  CCC
//
//  Created by Mustafa Ozhan on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import Provider

final class InterstitialAd: NSObject, GADFullScreenContentDelegate {
    func show() {
        GADInterstitialAd.load(
            withAdUnitID: SecretUtil.getSecret(key: "INTERSTITIAL_AD_ID"),
            request: GADRequest(),
            completionHandler: { interstitialAd, error in
                if let error = error {
                    let throwable = KotlinThrowable(
                        message: "InterstitialAd show \(error.localizedDescription)"
                    )
                    logger.e(throwable: throwable, tag: logger.tag, message: { String(describing: throwable.message) })
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
